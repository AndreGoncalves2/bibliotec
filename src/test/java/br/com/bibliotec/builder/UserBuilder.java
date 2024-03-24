package br.com.bibliotec.builder;

import br.com.bibliotec.controller.ReviewController;
import br.com.bibliotec.exeption.BibliotecException;
import br.com.bibliotec.model.Review;
import br.com.bibliotec.model.User;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class UserBuilder {
    
    private User user;
    
    public static UserBuilder build() {
        UserBuilder builder = new UserBuilder();
        builder.user = new User();
        
        builder.user.setAdmin(true);
        builder.user.setName("Test name");
        builder.user.setUsername("Test username"+ new Random().nextInt(10001));
        builder.user.setPassword("Test password");
        
        return builder;
    }
    
    private UserBuilder addReview(ReviewController reviewController) throws BibliotecException {
        Set<Review> reviewList = new HashSet<>(reviewController.list());
        
        if(!reviewList.isEmpty()) {
            this.user.setReviews(reviewList);
        } else {
            throw new BibliotecException("Lista de reviews vazia.");
        }
        
        return this;
    }
    
    public User now() {
        return user;
    }
}
