package org.example.model;

import jakarta.transaction.Transactional;
import org.example.entity.Book;
import org.example.repository.BookRepository;
import org.example.service.BookService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@Transactional
class BookServiceIntegrationTest {

    @Autowired
    private BookService bookService;

    @Autowired
    private BookRepository bookRepository;

    @Test
    void testSaveAndRetrieveBook() {
        Book book = new Book();
        book.setTitle("Integration Test Book");
        book.setAuthor("Test Author");

        // Save to the real database
        Book savedBook = bookService.saveBook(book);

        // Retrieve from the database
        Optional<Book> retrievedBook = bookService.getBookById(savedBook.getId());

        assertTrue(retrievedBook.isPresent());
        assertEquals("Integration Test Book", retrievedBook.get().getTitle());
    }
}

