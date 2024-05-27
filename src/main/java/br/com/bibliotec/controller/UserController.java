package br.com.bibliotec.controller;

import br.com.bibliotec.controller.helper.GenericController;
import br.com.bibliotec.exeption.BibliotecException;
import br.com.bibliotec.model.User;
import br.com.bibliotec.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserController extends GenericController<User, Long, UserRepository> {
    

    public UserController(@Autowired UserRepository repository) {
        super();
        this.repository = repository;
    }
    
    public User loadByEmail(String email) {
        return repository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado"));
    }

    @Override
    protected void validate(User entity, Mode mode) throws BibliotecException {
        if (mode.equals(Mode.SAVE) || mode.equals(Mode.UPDATE)) {
            User user = repository.findByEmail(entity.getEmail()).orElse(null);
            
            if (user != null && ! entity.getId().equals(user.getId())) {
                throw new BibliotecException("O endereço de email inserido já está em uso. Por favor, tente fazer login ou utilize outro email para se registrar.");
            }
        }
    }
}