package br.com.bibliotec.model;

import br.com.bibliotec.interfaces.HasId;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotEmpty;
import org.hibernate.validator.constraints.Length;

@Table
@Entity(name = "book")
public class Book implements HasId<Long> {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotEmpty(message = "Campo obrigatório")
    @Column(name = "code", updatable = false)
    private String code;

    @NotEmpty(message = "Campo obrigatório")
    @Column(name = "title", length = 100)
    private String title;
    
    @Column(name = "author", length = 60)
    private String author;

    @Length(max = 1000, message = "Limite de 1000 caracteres excedido.")
    @Column(name = "synopsis", length = 1000)
    private String synopsis;
    
    @Column(name = "string_image")
    private String stringImage;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String id) {
        this.code = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getSynopsis() {
        return synopsis;
    }

    public void setSynopsis(String synopsis) {
        this.synopsis = synopsis;
    }

    public String getStringImage() {
        return stringImage;
    }

    public void setStringImage(String stringImage) {
        this.stringImage = stringImage;
    }

    @Override
    public String toString() {
        return "Book{" +
                "code=" + code +
                ", title='" + title + '\'' +
                ", author='" + author + '\'' +
                '}';
    }
}
