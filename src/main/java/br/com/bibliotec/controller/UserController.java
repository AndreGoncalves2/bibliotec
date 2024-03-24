package br.com.bibliotec.controller;

import br.com.bibliotec.controller.helper.GenericController;
import br.com.bibliotec.model.User;
import br.com.bibliotec.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserController extends GenericController<User, Long, UserRepository> {

    public UserController(@Autowired UserRepository userRepository) {
        this.repository = userRepository;
    }
}
