package br.com.bibliotec.repository;

import br.com.bibliotec.model.Book;
import br.com.bibliotec.model.BookLoan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface BookLoanRepository extends JpaRepository<BookLoan, Long> {

    @Modifying
    @Transactional
    @Query("UPDATE book_loan bk SET bk.returned = :returned WHERE bk.id = :id")
    void updateReturned(@Param("id") Long id, @Param("returned") boolean returned);

    boolean existsByBookAndReturned(Book book, Boolean returned);
}
