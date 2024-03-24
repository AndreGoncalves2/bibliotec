package br.com.bibliotec.model;


import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

import java.time.LocalDateTime;


@Entity
@Table(name = "review")
public class Review {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@NotNull
	@ManyToOne
	@JoinColumn(name = "book_id", foreignKey = @ForeignKey(name = "review_book_fk"))
	private Book book;
	
	@NotBlank
	@Column(name = "title",
			length = 45)
	@Length(max = 45)
	private String title;
	
	@NotBlank
	@Column(name = "content", 
			nullable = false,
			columnDefinition = "LONGTEXT")
	private String content;
	
	@Min(value = 0)
	@Max(value = 5)
	@Column(name = "rating")
	private int rating;
	
	@NotNull
	@ManyToOne
	@JoinColumn(name = "user_id", foreignKey = @ForeignKey(name = "review_user_fk"))
	private User user;
	
	@Column(name = "created_at", nullable = false)
	private LocalDateTime createdAt;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}

	public Book getBook() {
		return book;
	}

	public void setBook(Book book) {
		this.book = book;
	}

	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public int getRating() {
		return rating;
	}
	public void setRating(int rating) {
		this.rating = rating;
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}
}
