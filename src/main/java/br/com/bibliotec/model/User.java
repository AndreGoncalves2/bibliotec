package br.com.bibliotec.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "user")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotBlank
    @Size(min = 3, max = 20, message = "O valor inserido deve ter entre 3 e 20 caracteres.")
    @Column(name = "username", length = 20)
    private String username;

    @NotBlank(message = "Campo obrigatório")
    @Column(name = "password")
    private String password;

    @NotBlank(message = "Campo obrigatório")
    @Email(message = "Email inválido")
    @Column(name = "email", length = 60, unique = true)
    private String email;

    public String getUsername() {
        return username;
    }

    public User() {}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
