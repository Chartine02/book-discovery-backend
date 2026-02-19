package com.noella.bookdiscovery.dto;

import jakarta.validation.constraints.*;
import lombok.*;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookRequest {

    @NotBlank(message = "Title is required")
    private String title;

    @NotBlank(message = "Author is required")
    private String author;

    private String coverUrl;

    @NotBlank(message = "Genre is required")
    private String genre;

    @NotBlank(message = "Published date is required")
    private String publishedAt;

    @DecimalMin("0.0") @DecimalMax("5.0")
    private Double rating;

    @Min(0)
    private Integer reviewCount;

    @NotBlank(message = "Description is required")
    private String description;

    @Min(0)
    private Integer readCount;
};
