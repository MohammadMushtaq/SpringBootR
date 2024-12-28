package org.example.model;

import org.example.entity.Book;
import org.example.repository.BookRepository;
import org.example.service.BookService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.stubbing.OngoingStubbing;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class BookControllerTest {

    @InjectMocks
    BookController controller;

    @Mock
    BookService bookService;

    @Mock
    BookRepository bookRepository;

        Book book = new Book();

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateBook() {
        book.setId(123L);
        book.setTitle("1234");
        book.setAuthor("mushtaq");

        when(bookService.saveBook(book)).thenReturn(book);

        Book controllerBook = controller.createBook(book);

        assertEquals(book.getId(), controllerBook.getId(), "Book ID should match");
        assertEquals(book.getTitle(), controllerBook.getTitle(), "Book title should match");
        assertEquals(book.getAuthor(), controllerBook.getAuthor(), "Book author should match");
        verify(bookService).saveBook(book);
    }

    @Test
    void testByIDifBookIsPresent(){
        book.setId(123L);
       Long id= book.getId();
        when(bookService.getBookById(id)).thenReturn(Optional.of(book));
        ResponseEntity<Book> response = controller.getBookByID(id);
        assertEquals(ResponseEntity.ok(book).getStatusCode(),response.getStatusCode());
        assertNotNull(response.getBody());
    }
    @Test
    void testByIDifBookIsNotPresent() {
        // Arrange: Define a non-existent book ID
        Long nonExistentId = 999L;
        when(bookService.getBookById(nonExistentId)).thenReturn(Optional.empty());
        ResponseEntity<Book> response = controller.getBookByID(nonExistentId);
        // Assert: Check that the response status is 404 Not Found
        assertEquals(ResponseEntity.notFound().build().getStatusCode(), response.getStatusCode());
        assertNull(response.getBody()); // The body should be null for a 404 response
    }

    @Test
    void testAllBooksArePresent(){
       ArrayList<Book> books = new ArrayList<Book>();
       books.add(new Book());
       books.add(new Book());
       books.add(new Book());
       when(bookService.getAllBooks()).thenReturn(new ArrayList<>(books));
       List<Book> outputBook = controller.getAllBook();
       assertNotNull(outputBook);
       assertEquals(3,outputBook.size());
    }

    @Test
    void testDeleteBookByIDWhenBookExists() {
        // Arrange: Define an existing book ID
        Long existingId = 112L;
        when(bookService.getBookById(existingId)).thenReturn(Optional.of(new Book()));
        doNothing().when(bookService).deleteBook(existingId);
        ResponseEntity<Void> response = controller.deleteBookByID(existingId);
        verify(bookService).deleteBook(existingId);
        assertEquals(ResponseEntity.noContent().build().getStatusCode(), response.getStatusCode());
    }
    @Test
    void testDeleteBookByIDWhenBookDoesNotExist() {
        // Arrange: Define a non-existent book ID
        Long nonExistentId = 999L;
        // Mock behavior: Simulate the book does not exist
        when(bookService.getBookById(nonExistentId)).thenReturn(Optional.empty());
        ResponseEntity<Void> response = controller.deleteBookByID(nonExistentId);
        verify(bookService, never()).deleteBook(nonExistentId);
        assertEquals(ResponseEntity.notFound().build().getStatusCode(), response.getStatusCode());
    }

    @Test
    void testUpdateBookByIDWhenBookExists(){
        Long id = 33L;
        Book book = new Book();
        book.setTitle("ff");
        book.setAuthor("dd");
        when(bookService.getBookById(id)).thenReturn(Optional.of(book));
        when(bookService.saveBook(book)).thenReturn(book);
        ResponseEntity<Book> response= controller.updateBook(id,book);
        assertEquals(ResponseEntity.ok(book).getStatusCode(),response.getStatusCode());
        assertEquals(book,response.getBody());
        verify(bookService).saveBook(book);
    }
    @Test
    void testUpdateBookByIDWhenBookDoesNotExist() {
        // Arrange: Define a non-existent book ID
        Long id = 33L;
        Book book = new Book();
        book.setTitle("ff");
        book.setAuthor("dd");

        when(bookService.getBookById(id)).thenReturn(Optional.empty());
        ResponseEntity<Book> response = controller.updateBook(id, book);
        assertEquals(ResponseEntity.notFound().build().getStatusCode(), response.getStatusCode());
        verify(bookService, never()).saveBook(book);
    }

}