package com.noella.bookdiscovery.dto;

import lombok.*;

import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookResponse {
    private Long id;
    private String title;
    private String author;
    private String coverUrl;
    private String genre;
    private String publishedAt;
    private Double rating;
    private Integer reviewCount;
    private String description;
    private Integer readCount;
    private Instant createdAt;
    private Instant updatedAt;
}
