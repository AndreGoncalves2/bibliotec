package br.com.bibliotec.controller;

import br.com.bibliotec.controller.helper.GenericController;
import br.com.bibliotec.exception.DuplicateRaException;
import br.com.bibliotec.model.Student;
import br.com.bibliotec.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StudentController extends GenericController<Student, Long, StudentRepository> {
    
    public StudentController(@Autowired StudentRepository repository) {
        this.repository = repository;
    }
    
    public boolean existsByRa(String ra) {
        return repository.existsAllByRa(ra);
    }
    
    @Override
    protected void validate(Student entity, Mode mode) throws DuplicateRaException {
        if(mode.equals(Mode.SAVE)) {
            if(existsByRa(entity.getRa())){
                throw new DuplicateRaException("Ra já cadastrado");
            }
        }
    }
}
