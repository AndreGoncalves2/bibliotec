package br.com.bibliotec.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.Set;

@Entity
@Table(name = "user")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotBlank
    @Size(min = 3, max = 20, message = "O valor inserido deve ter entre 3 e 20 caracteres.")
    @Column(name = "username", length = 20, unique = true)
    private String username;

    @NotBlank(message = "Campo obrigatório")
    @Size(min = 3, max = 20, message = "O valor inserido deve ter entre 3 e 20 caracteres.")
    @Column(name = "name", length = 20)
    private String name;

    @NotBlank(message = "Campo obrigatório")
    @Column(name = "password")
    private String password;
    
    @Column(name = "is_admin", nullable = false)
    private boolean isAdmin;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "user")
    private Set<Review> reviews;  

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getName() {
        return name;
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

    public boolean isAdmin() {
        return isAdmin;
    }

    public void setAdmin(boolean admin) {
        isAdmin = admin;
    }

    public Set<Review> getReviews() {
        return reviews;
    }

    public void setReviews(Set<Review> reviews) {
        if(reviews != null) {
            reviews.forEach(row -> row.setUser(this));
        }
        this.reviews = reviews;
    }
}
