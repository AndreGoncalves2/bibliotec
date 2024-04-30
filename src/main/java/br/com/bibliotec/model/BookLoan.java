package br.com.bibliotec.model;

import br.com.bibliotec.interfaces.HasId;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

@Table
@Entity(name = "book_loan")
public class BookLoan implements HasId<Long> {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    
    @ManyToOne
    @NotNull
    @JoinColumn(name = "book")
    private Book book;
    
    @NotNull
    @Column(name = "returned")
    private Boolean returned;
    
    @NotNull
    @Column(name = "booking_date")
    private LocalDate bookingDate;
    
    @Column(name = "due_date")
    private LocalDate dueDate;   
    
    @NotNull
    @ManyToOne
    @JoinColumn(name = "student_ra")
    private Student student;

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

    public Boolean getReturned() {
        return returned;
    }

    public void setReturned(Boolean reserved) {
        this.returned = reserved;
    }

    public LocalDate getBookingDate() {
        return bookingDate;
    }

    public void setBookingDate(LocalDate bookingDate) {
        this.bookingDate = bookingDate;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    @Override
    public String toString() {
        return "BookLoan{" +
                "id=" + id +
                ", book=" + book +
                ", student=" + student +
                '}';
    }
}
