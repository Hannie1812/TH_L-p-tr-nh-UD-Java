package com.nbhang.viewmodels;

import com.nbhang.entities.Book;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Builder;

@Builder
public record BookPostVm(
        @NotBlank(message = "Title must not be blank")
        @Size(min = 1, max = 50, message = "Title must be between 1 and 50 characters")
        String title,
        
        @NotBlank(message = "Author must not be blank")
        @Size(min = 1, max = 50, message = "Author must be between 1 and 50 characters")
        String author,
        
        @NotNull(message = "Price must not be null")
        @Positive(message = "Price must be greater than 0")
        Double price,
        
        @NotNull(message = "Category ID must not be null")
        Long categoryId) {
    public static BookPostVm from(@NotNull Book book) {
        return new BookPostVm(book.getTitle(), book.getAuthor(),
                book.getPrice(), book.getCategory().getId());
    }
}
