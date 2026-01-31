package com.nbhang.controllers;

import com.nbhang.entities.Book;
import com.nbhang.services.BookService;
import com.nbhang.entities.Category;
import com.nbhang.services.CategoryService;
import com.nbhang.services.CartService;
import com.nbhang.daos.Item;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/books")
@RequiredArgsConstructor
public class BookController {
        private final BookService bookService;
        private final CategoryService categoryService;
        private final CartService cartService;

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
        public String showAddForm(Model model) {
                model.addAttribute("book", new Book());
                model.addAttribute("categories", categoryService.getAllCategories());
                return "book/add";
        }

        @PostMapping("/add")
        public String addBook(@ModelAttribute("book") Book book, RedirectAttributes redirectAttributes) {
                try {
                        bookService.addBook(book);
                        redirectAttributes.addFlashAttribute("successMessage", "Book added successfully!");
                        return "redirect:/books";
                } catch (Exception e) {
                        redirectAttributes.addFlashAttribute("errorMessage", "Add book failed: " + e.getMessage());
                        return "redirect:/books/add";
                }
        }

        @GetMapping("/edit/{id}")
        public String showEditForm(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
                try {
                        Book book = bookService.getBookById(id);
                        if (book != null) {
                                model.addAttribute("book", book);
                                model.addAttribute("categories", categoryService.getAllCategories());
                                return "book/edit";
                        } else {
                                redirectAttributes.addFlashAttribute("errorMessage", "Book not found!");
                                return "redirect:/books";
                        }
                } catch (Exception e) {
                        redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
                        return "redirect:/books";
                }
        }

        @PostMapping("/edit")
        public String editBook(@ModelAttribute("book") Book book, RedirectAttributes redirectAttributes) {
                try {
                        bookService.updateBook(book);
                        redirectAttributes.addFlashAttribute("successMessage", "Book updated successfully!");
                        return "redirect:/books";
                } catch (Exception e) {
                        redirectAttributes.addFlashAttribute("errorMessage", "Update book failed: " + e.getMessage());
                        return "redirect:/books/edit/" + book.getId();
                }
        }

        @GetMapping("/delete/{id}")
        public String deleteBook(@PathVariable Long id, RedirectAttributes redirectAttributes) {
                try {
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
}