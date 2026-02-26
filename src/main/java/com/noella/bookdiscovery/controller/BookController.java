package com.noella.bookdiscovery.controller;

import com.noella.bookdiscovery.dto.BookRequest;
import com.noella.bookdiscovery.dto.BookResponse;
import com.noella.bookdiscovery.service.BookService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/books")
@RequiredArgsConstructor
public class BookController {

    public final BookService bookService;

    @GetMapping
    public List<BookResponse> getAllBooks(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) String genre,
            @RequestParam(required = false) String timeFilter,
            @RequestParam(defaultValue = "publishedAt") String sort,
            @RequestParam(defaultValue = "desc") String order
    ){
        return bookService.getAllBooks(search, genre, timeFilter, sort, order);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BookResponse> getBookById(@PathVariable Long id){
        return bookService.getBookbyId(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<BookResponse> postBook(@Valid @RequestBody BookRequest request){

        BookResponse book = bookService.createBook(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(book);
    }

    @PutMapping("/{id}")
    public ResponseEntity<BookResponse> deleteBook(
           @PathVariable Long id,
           @RequestBody BookRequest request
    ){
        return bookService.updateBook(id, request).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<BookResponse> deleteBook(@PathVariable Long id){
        if(bookService.deleteBook(id)){
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }


}
