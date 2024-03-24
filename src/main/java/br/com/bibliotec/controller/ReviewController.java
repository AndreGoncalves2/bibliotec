package br.com.bibliotec.controller;

import br.com.bibliotec.controller.helper.GenericController;
import br.com.bibliotec.model.Review;
import br.com.bibliotec.repository.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ReviewController extends GenericController<Review, Long, ReviewRepository> {

    public ReviewController(@Autowired ReviewRepository reviewRepository) {
        this.repository = reviewRepository;
    }
}
