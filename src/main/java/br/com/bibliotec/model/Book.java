package br.com.bibliotec.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

@Table
@Entity(name = "book")
public class Book {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    
    @NotBlank
    @Column(name = "title", length = 100)
    private String title;
    
    @Column(name = "author", length = 60)
    private String author;
    
    @NotNull
    @Column(name = "number_pages")
    private int numberPages;

    @NotNull
    @Column(name = "image", columnDefinition = "mediumblob")
    private byte[] imagem;

    @Length(max = 1000)
    @Column(name = "synopsis",length = 1000)
    private String synopsis;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public int getNumberPages() {
        return numberPages;
    }

    public void setNumberPages(int numberPages) {
        this.numberPages = numberPages;
    }

    public byte[] getImagem() {
        return imagem;
    }

    public void setImagem(byte[] imagem) {
        this.imagem = imagem;
    }

    public String getSynopsis() {
        return synopsis;
    }

    public void setSynopsis(String synopsis) {
        this.synopsis = synopsis;
    }
}