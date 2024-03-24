package br.com.bibliotec.builder;

import br.com.bibliotec.controller.BookController;
import br.com.bibliotec.controller.UserController;
import br.com.bibliotec.exeption.BibliotecException;
import br.com.bibliotec.model.Book;
import br.com.bibliotec.model.Review;
import br.com.bibliotec.model.User;

import java.time.LocalDateTime;
import java.util.List;

public class ReviewBuilder {
    
    private Review review;
    
    public static ReviewBuilder build() {
        ReviewBuilder builder = new ReviewBuilder();
        builder.review = new Review();
        
        builder.review.setContent("Test Content");
        builder.review.setRating(4);
        builder.review.setTitle("Test Title");
        builder.review.setCreatedAt(LocalDateTime.now());
        
        return builder;
    }
    
    public ReviewBuilder addBook(BookController bookController) throws BibliotecException {
        List<Book> bookList = bookController.list();
        
        if(bookList.isEmpty()) {
            this.review.setBook(bookController.save(BookBuilder.build().now()));
        } else {
            this.review.setBook(bookList.getFirst());
        }
        
        return this;
    }
    
    public ReviewBuilder addUser(UserController userController) throws BibliotecException {
        List<User> userList = userController.list();
        
        if(userList.isEmpty()) {
            this.review.setUser(userController.save(UserBuilder.build().now()));
        } else {
            this.review.setUser(userList.getFirst());
        }
        
        return this;
    }
    
    public Review now() {
        return review;
    }
}
