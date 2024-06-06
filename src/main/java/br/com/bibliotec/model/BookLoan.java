package br.com.bibliotec.model;

import br.com.bibliotec.interfaces.HasId;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
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
    @NotNull(message = "Selecione o livro.")
    @JoinColumn(name = "book")
    private Book book;
    
    @NotNull
    @Column(name = "returned")
    private Boolean returned;
    
    @NotNull(message = "Campo obrigatório")
    @Column(name = "booking_date")
    private LocalDate bookingDate;
    
    @Column(name = "due_date")
    private LocalDate dueDate;   
    
    @NotNull(message = "Selecione o aluno.")
    @ManyToOne
    @JoinColumn(name = "student_ra")
    private Student student;
    
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

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

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
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
