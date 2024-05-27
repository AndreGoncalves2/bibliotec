package br.com.bibliotec.authentication;

import br.com.bibliotec.controller.UserController;
import br.com.bibliotec.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService implements UserDetailsService {
    
    @Autowired
    private UserController userController;

    private static PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    
    public UserService(UserController userController) {
        this.userController = userController;
    }
    
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        if (passwordEncoder == null)
            passwordEncoder = new BCryptPasswordEncoder();

        User user = userController
                .loadByEmail(email);

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return org.springframework.security.core.userdetails.User.withUsername(user.getEmail())
                .password("{bcrypt}" + user.getPassword()).build();
    }
    
    public User getLoggedUser() {
        SecurityService securityService = new SecurityService();
        try {
            return userController.loadByEmail(securityService.getAuthenticatedUser().getUsername());
        } catch (UsernameNotFoundException e) {
            return null;
        }
    }

    public static String encodePassword(String password) {
        return passwordEncoder.encode(password);
    }

    public static boolean matches(String rawPassword, String encodedPassword) {
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }
    
}
