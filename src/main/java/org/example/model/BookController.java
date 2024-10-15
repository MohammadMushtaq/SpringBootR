package org.example.model;

import org.example.entity.Book;
import org.example.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/books")
public class BookController {

   @Autowired
   private BookService bookService;

    @PostMapping
   public Book createBook(@RequestBody Book book){
        return bookService.saveBook(book);
   }

    @GetMapping("/{id}")
    public ResponseEntity<Book> getBookByID(@PathVariable Long id){
        Optional<Book> book = bookService.getBookById(id);
        return book.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
   }

    @GetMapping
   public List<Book> getAllBook(){
       return bookService.getAllBooks();
   }

   @DeleteMapping("/{id}")
   public ResponseEntity<Void> deleteBookByID(@PathVariable Long id){
       if(bookService.getBookById(id).isPresent()){
           bookService.deleteBook(id);
           return ResponseEntity.noContent().build();
       }
       return ResponseEntity.notFound().build();
   }

    @PutMapping("/{id}")
    public ResponseEntity<Book> updateBook(@PathVariable Long id, @RequestBody Book book){
        if(bookService.getBookById(id).isPresent()){
            book.setId(id);
            Book updateBook = bookService.saveBook(book);
            return ResponseEntity.ok(updateBook);

        }
        return ResponseEntity.notFound().build();
    }
}
