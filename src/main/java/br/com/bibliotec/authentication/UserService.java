package br.com.bibliotec.authentication;

import br.com.bibliotec.exeption.BibliotecException;
import br.com.bibliotec.model.User;
import br.com.bibliotec.repository.UserRepository;
import com.vaadin.flow.component.UI;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService implements UserDetailsService {

    private final UserRepository repository;

    private static PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public UserService(UserRepository repository) {
        this.repository = repository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        if (passwordEncoder == null)
            passwordEncoder = new BCryptPasswordEncoder();

        User user = repository
                .findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado"));

        user.setPassword(passwordEncoder.encode(user.getPassword()));

        return org.springframework.security.core.userdetails.User.withUsername(user.getUsername())
                .password("{bcrypt}" + user.getPassword()).build();
    }

    public static String encodePassword(String password) {
        return passwordEncoder.encode(password);
    }

    public static boolean matches(String rawPassword, String encodedPassword) {
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }

    public User getAuthenticatedUser() throws BibliotecException {
        SecurityService securityService = new SecurityService();
        String username = securityService.getAuthenticatedUser().getUsername();

        Optional<User> user = repository.findByUsername(username);

        if (user.isPresent()) {
            return user.get();
        } else {
            UI.getCurrent().navigate("/logout");
            throw new BibliotecException("Usuário não autenticado");
        }
    }
}
