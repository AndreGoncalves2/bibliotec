package br.com.bibliotec.repository;

import br.com.bibliotec.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {
    
    boolean existsAllByRa(String ra);
}
