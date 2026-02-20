package com.noella.bookdiscovery.service;

import com.noella.bookdiscovery.dto.BookRequest;
import com.noella.bookdiscovery.dto.BookResponse;
import com.noella.bookdiscovery.model.Book;
import com.noella.bookdiscovery.repository.BookRepository;
import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BookService {

    private final BookRepository bookRepository;

    public List<BookResponse> getAllBooks(String search, String genre, String timeFilter, String sort, String order){
        Specification<Book> spec = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (search != null && !search.isBlank()){
                String pattern = "%" + search.trim().toLowerCase() + "%";
                predicates.add(cb.or(
                        cb.like(cb.lower(root.get("title")), pattern),
                        cb.like(cb.lower(root.get("author")), pattern)
                ));
            }

            if(genre != null && !genre.isBlank()){
                predicates.add(cb.equal(root.get("genre"), genre.trim()));
            }

            if(timeFilter != null && !timeFilter.equals("all")){
                LocalDate startDate = switch (timeFilter) {
                    case "week" -> LocalDate.now().minusWeeks(1);
                    case "month" -> LocalDate.now().minusMonths(1);
                    case "year" -> LocalDate.now().minusYears(1);
                    default -> null;
                };
                if(startDate != null){
                    predicates.add(cb.greaterThanOrEqualTo(root.get("publishedAt"), startDate.toString()
                    ));
                }
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };

        String sortField = List.of("publishedAt", "rating", "readCount").contains(sort) ? sort : "publishedAt";

        Sort.Direction direction = "asc".equals(order) ? Sort.Direction.ASC : Sort.Direction.DESC;

        return bookRepository.findAll(spec, Sort.by(direction, sortField))
                .stream()
                .map(this::toResponse)
                .toList();

    };

    public Optional<BookResponse> getBookbyId(Long id){
        return bookRepository.findById(id).map(this::toResponse);
    };

    public BookResponse createBook(BookRequest req){
      Book book = Book.builder()
              .title(req.getTitle())
              .author(req.getAuthor())
              .coverUrl(req.getCoverUrl())
              .genre(req.getGenre())
              .publishedAt(req.getPublishedAt())
              .rating(req.getRating() != null ? req.getRating() : 0.0)
              .reviewCount(req.getReviewCount() != null ? req.getReviewCount() : 0)
              .description(req.getDescription())
              .readCount(req.getReadCount() != null ? req.getReadCount() : 0)
              .build();

      return toResponse(bookRepository.save(book));
    };

    public Optional<BookResponse> updateBook(Long id, BookRequest req){

        return bookRepository.findById(id).map(book ->{
            if(req.getTitle() != null)        book.setTitle(req.getTitle());
            if(req.getAuthor() != null)       book.setAuthor(req.getAuthor());
            if(req.getCoverUrl() != null)     book.setCoverUrl(req.getCoverUrl());
            if (req.getGenre() != null)       book.setGenre(req.getGenre());
            if (req.getPublishedAt() != null) book.setPublishedAt(req.getPublishedAt());
            if (req.getRating() != null)      book.setRating(req.getRating());
            if (req.getReviewCount() != null) book.setReviewCount(req.getReviewCount());
            if (req.getDescription() != null) book.setDescription(req.getDescription());
            if (req.getReadCount() != null)   book.setReadCount(req.getReadCount());

            return toResponse(bookRepository.save(book));
        });
    }

    public boolean deleteBook(Long id){
        if(!bookRepository.existsById(id)) return false;
        bookRepository.deleteById(id);
        return true;
    }

    private BookResponse toResponse(@NonNull Book book) {
        return BookResponse.builder()
                .id(book.getId())
                .title(book.getTitle())
                .author(book.getAuthor())
                .coverUrl(book.getCoverUrl())
                .genre(book.getGenre())
                .publishedAt(book.getPublishedAt())
                .rating(book.getRating())
                .reviewCount(book.getReviewCount())
                .description(book.getDescription())
                .readCount(book.getReadCount())
                .createdAt(book.getCreatedAt())
                .updatedAt(book.getUpdatedAt())
                .build();
    }
}
