package com.noella.bookdiscovery.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.*;

import java.time.Instant;

@Entity
@Table(name = "book")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Title is required")
    @Column(nullable = false)
    private String title;

    @NotBlank(message = "Author is required")
    @Column(nullable = false)
    private String author;

    @Column(columnDefinition = "VARCHAR(255) DEFAULT 'book-stack.jpg'")
    @Builder.Default
    private String coverUrl = "book-stack.jpg";

    @NotBlank(message = "Genre is required")
    @Column(nullable = false)
    private String genre;

    @NotBlank(message = "Published date is required")
    @Column(nullable = false)
    private String publishedAt;

    @DecimalMin("0.0") @DecimalMax("5.0")
    @Builder.Default
    private Double rating = 0.0;

    @Min(0)
    @Builder.Default
    private Integer reviewCount = 0;

    @NotBlank(message = "Description is required")
    @Column(columnDefinition = "TEXT", nullable = false)
    private String description;

    @CreationTimestamp
    @Column(updatable = false)
    private Instant createdAt;

    @UpdateTimestamp
    private Instant updatedAt;
}
