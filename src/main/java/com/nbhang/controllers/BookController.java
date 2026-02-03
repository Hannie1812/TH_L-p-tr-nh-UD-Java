package com.nbhang.controllers;

import com.nbhang.entities.Book;
import com.nbhang.services.BookService;
import com.nbhang.entities.Category;
import com.nbhang.services.CategoryService;
import com.nbhang.services.CartService;
import com.nbhang.services.FileStorageService;
import com.nbhang.daos.Item;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;

import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/books")
@RequiredArgsConstructor
public class BookController {
        private final BookService bookService;
        private final CategoryService categoryService;
        private final CartService cartService;
        private final FileStorageService fileStorageService;

        @GetMapping
        public String showAllBooks(
                        @NotNull Model model,
                        @RequestParam(defaultValue = "0") Integer pageNo,
                        @RequestParam(defaultValue = "20") Integer pageSize,
                        @RequestParam(defaultValue = "id") String sortBy) {
                model.addAttribute("books", bookService.getAllBooks(pageNo,
                                pageSize, sortBy));
                model.addAttribute("currentPage", pageNo);
                model.addAttribute("categories",
                                categoryService.getAllCategories());
                model.addAttribute("totalPages",
                                bookService.getAllBooks(pageNo, pageSize, sortBy).size() / pageSize);
                return "book/list";
        }

        @GetMapping("/add")
        public String addBookForm(@NotNull Model model) {
                model.addAttribute("book", new Book());
                model.addAttribute("categories",
                                categoryService.getAllCategories());
                return "book/add";
        }

        @PostMapping("/add")
        public String addBook(
                        @Valid @ModelAttribute("book") Book book,
                        @NotNull BindingResult bindingResult,
                        @RequestParam(value = "imageFile", required = false) org.springframework.web.multipart.MultipartFile imageFile,
                        Model model) {
                if (bindingResult.hasErrors()) {
                        var errors = bindingResult.getAllErrors()
                                        .stream()
                                        .map(DefaultMessageSourceResolvable::getDefaultMessage)
                                        .toArray(String[]::new);
                        model.addAttribute("errors", errors);
                        model.addAttribute("categories",
                                        categoryService.getAllCategories());
                        return "book/add";
                }

                System.out.println("========== ADD BOOK ==========");
                System.out.println("ImageFile: " + imageFile);
                if (imageFile != null) {
                        System.out.println("  - Name: " + imageFile.getOriginalFilename());
                        System.out.println("  - Size: " + imageFile.getSize());
                        System.out.println("  - Empty: " + imageFile.isEmpty());
                }

                // Save book first to get the ID
                Book savedBook = bookService.addBook(book);
                System.out.println("Book saved with ID: " + savedBook.getId());

                // Handle image upload if provided
                if (imageFile != null && !imageFile.isEmpty()) {
                        System.out.println("Uploading image...");
                        try {
                                String filename = fileStorageService.storeFile(imageFile, savedBook.getId());
                                System.out.println("File stored: " + filename);
                                savedBook.setImageUrl(filename);
                                bookService.updateBook(savedBook);
                                System.out.println("Book updated with imageUrl");
                        } catch (Exception e) {
                                System.err.println("Upload failed: " + e.getMessage());
                                e.printStackTrace();
                                model.addAttribute("error", "Failed to upload image: " + e.getMessage());
                        }
                } else {
                        System.out.println("No image to upload");
                }

                return "redirect:/books";
        }

        @GetMapping("/edit/{id}")
        public String editBookForm(@NotNull Model model, @PathVariable long id) {
                var book = bookService.getBookById(id);
                model.addAttribute("book", book.orElseThrow(() -> new IllegalArgumentException("Book not found")));
                model.addAttribute("categories", categoryService.getAllCategories());
                return "book/edit";
        }

        @PostMapping("/edit")
        public String editBook(@Valid @ModelAttribute("book") Book book,
                        @NotNull BindingResult bindingResult,
                        @RequestParam(value = "imageFile", required = false) org.springframework.web.multipart.MultipartFile imageFile,
                        Model model) {
                if (bindingResult.hasErrors()) {
                        var errors = bindingResult.getAllErrors()
                                        .stream()
                                        .map(DefaultMessageSourceResolvable::getDefaultMessage)
                                        .toArray(String[]::new);
                        model.addAttribute("errors", errors);
                        model.addAttribute("categories",
                                        categoryService.getAllCategories());
                        return "book/edit";
                }

                System.out.println("========== EDIT BOOK ==========");
                System.out.println("Book ID: " + book.getId());
                System.out.println("Current imageUrl from form: " + book.getImageUrl());
                System.out.println("ImageFile parameter: " + imageFile);
                if (imageFile != null) {
                        System.out.println("  - Name: " + imageFile.getOriginalFilename());
                        System.out.println("  - Size: " + imageFile.getSize());
                        System.out.println("  - Empty: " + imageFile.isEmpty());
                }

                // Handle image upload if new image provided
                if (imageFile != null && !imageFile.isEmpty()) {
                        System.out.println("Uploading new image...");
                        try {
                                // Get old image URL to delete later
                                var existingBook = bookService.getBookById(book.getId());
                                String oldImageUrl = existingBook.map(Book::getImageUrl).orElse(null);
                                System.out.println("Old imageUrl from DB: " + oldImageUrl);

                                // Save new image
                                String filename = fileStorageService.storeFile(imageFile, book.getId());
                                System.out.println("New file stored: " + filename);
                                book.setImageUrl(filename);

                                // Delete old image if exists
                                if (oldImageUrl != null && !oldImageUrl.isEmpty()) {
                                        fileStorageService.deleteFile(oldImageUrl);
                                        System.out.println("Old image deleted: " + oldImageUrl);
                                }
                        } catch (Exception e) {
                                System.err.println("Upload failed: " + e.getMessage());
                                e.printStackTrace();
                                model.addAttribute("error", "Failed to upload image: " + e.getMessage());
                        }
                } else {
                        System.out.println("No new image - keeping existing imageUrl: " + book.getImageUrl());
                }

                bookService.updateBook(book);
                System.out.println("Book updated with final imageUrl: " + book.getImageUrl());
                return "redirect:/books";
        }

        @GetMapping("/delete/{id}")
        public String deleteBook(@PathVariable Long id, RedirectAttributes redirectAttributes) {
                try {
                        // Get book to delete associated image
                        var book = bookService.getBookById(id);
                        if (book.isPresent() && book.get().getImageUrl() != null) {
                                fileStorageService.deleteFile(book.get().getImageUrl());
                        }

                        bookService.deleteBookById(id);
                        redirectAttributes.addFlashAttribute("successMessage", "Book deleted successfully!");
                        return "redirect:/books";
                } catch (Exception e) {
                        redirectAttributes.addFlashAttribute("errorMessage", "Delete book failed: " + e.getMessage());
                        return "redirect:/books";
                }
        }

        @PostMapping("/add-to-cart")
        public String addToCart(HttpSession session,
                        @RequestParam long id,
                        @RequestParam String name,
                        @RequestParam double price,
                        @RequestParam(defaultValue = "1") int quantity) {
                var cart = cartService.getCart(session);
                cart.addItems(new Item(id, name, price, quantity));
                cartService.updateCart(session, cart);
                return "redirect:/books";
        }

        @GetMapping("/search")
        public String searchBook(
                        @NotNull Model model,
                        @RequestParam String keyword,
                        @RequestParam(defaultValue = "0") Integer pageNo,
                        @RequestParam(defaultValue = "20") Integer pageSize,
                        @RequestParam(defaultValue = "id") String sortBy) {
                model.addAttribute("books", bookService.searchBook(keyword));
                model.addAttribute("currentPage", pageNo);
                model.addAttribute("totalPages",
                                bookService
                                                .getAllBooks(pageNo, pageSize, sortBy)
                                                .size() / pageSize);
                model.addAttribute("categories",
                                categoryService.getAllCategories());
                return "book/list";
        }
}