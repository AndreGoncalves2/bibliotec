package br.com.bibliotec.builder;

import br.com.bibliotec.model.Book;

import java.util.Random;

public class BookBuilder {
    
    private Book book;
    
    public static BookBuilder build() {
        BookBuilder builder = new BookBuilder();
        builder.book = new Book();
        
        builder.book.setCode(String.valueOf(new Random().nextInt(1001)));
        builder.book.setAuthor("TestAuthor");
        builder.book.setSynopsis("TestSynopsis");
        builder.book.setTitle("TestTitle");
        
        return builder;
    }
    
    public Book now() {
        return book;
    }
}
