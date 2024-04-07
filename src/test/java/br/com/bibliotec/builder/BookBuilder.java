package br.com.bibliotec.builder;

import br.com.bibliotec.model.Book;

import java.util.Random;

public class BookBuilder {
    
    private Book book;
    
    public static BookBuilder build() {
        BookBuilder builder = new BookBuilder();
        builder.book = new Book();
        
        builder.book.setCode(new Random().nextInt(1001));
        builder.book.setAuthor("TestAuthor");
        builder.book.setSynopsis("TestSynopsis");
        builder.book.setTitle("TestTitle");
        builder.book.setImage( new byte[]{ 0x12, 0x34, 0x56, 0x78 });
        
        return builder;
    }
    
    public Book now() {
        return book;
    }
}
