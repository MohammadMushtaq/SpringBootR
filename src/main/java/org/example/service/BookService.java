package org.example.service;

import org.example.entity.Book;
import org.example.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.Optional;

@Service
public class BookService {

    @Autowired
    BookRepository bookRepository;


    public Book saveBook(Book book){
        return bookRepository.save(book);
    }


    public List<Book> getAllBooks(){
        return bookRepository.findAll();
    }


    public Optional<Book> getBookById(Long id){
        return bookRepository.findById(id);
    }

    public void deleteBook(Long id){
        bookRepository.deleteById(id);
    }

}
