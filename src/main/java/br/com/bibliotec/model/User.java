package br.com.bibliotec.model;

import br.com.bibliotec.interfaces.HasId;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "user")
public class User implements HasId<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id = 0L;

    @NotEmpty(message = "Campo obrigatório")
    @Size(min = 3, max = 20, message = "O valor inserido deve ter entre 3 e 20 caracteres.")
    @Column(name = "name", length = 20)
    private String name;
    
    @NotEmpty(message = "Campo obrigatório")
    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-zA-Z]).{8}.*$",
            message = "Senha deve ter no mínimo 8 caracteres, incluindo uma letra e um dígito.")
    @Column(name = "password")
    private String password;

    @NotEmpty(message = "Campo obrigatório")
    @Email(message = "Email inválido")
    @Column(name = "email", length = 60, unique = true)
    private String email;

    public String getName() {
        return name;
    }

    public User() {}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
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
