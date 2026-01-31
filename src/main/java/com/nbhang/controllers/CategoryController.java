package com.nbhang.controllers;

import com.nbhang.entities.Category;
import com.nbhang.services.CategoryService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/categories")
public class CategoryController {
    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    // Hiển thị danh sách tất cả danh mục
    @GetMapping
    public String listCategories(Model model) {
        List<Category> categories = categoryService.getAllCategories();
        model.addAttribute("categories", categories);
        return "category/list";
    }

    // Hiển thị form thêm danh mục mới
    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("category", new Category());
        return "category/add";
    }

    // Xử lý thêm danh mục mới
    @PostMapping("/add")
    public String addCategory(@ModelAttribute("category") Category category, 
                             RedirectAttributes redirectAttributes) {
        try {
            categoryService.addCategory(category);
            redirectAttributes.addFlashAttribute("successMessage", "Thêm danh mục thành công!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Thêm danh mục thất bại: " + e.getMessage());
        }
        return "redirect:/categories";
    }

    // Hiển thị form sửa danh mục
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model, 
                              RedirectAttributes redirectAttributes) {
        try {
            Category category = categoryService.getCategoryById(id)
                    .orElseThrow(() -> new RuntimeException("Danh mục không tồn tại"));
            model.addAttribute("category", category);
            return "category/edit";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/categories";
        }
    }

    // Xử lý cập nhật danh mục
    @PostMapping("/edit")
    public String updateCategory(@ModelAttribute("category") Category category,
                                RedirectAttributes redirectAttributes) {
        try {
            categoryService.updateCategory(category);
            redirectAttributes.addFlashAttribute("successMessage", "Cập nhật danh mục thành công!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Cập nhật danh mục thất bại: " + e.getMessage());
        }
        return "redirect:/categories";
    }

    // Xóa danh mục
    @GetMapping("/delete/{id}")
    public String deleteCategory(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            if (categoryService.existsById(id)) {
                categoryService.deleteCategoryById(id);
                redirectAttributes.addFlashAttribute("successMessage", "Xóa danh mục thành công!");
            } else {
                redirectAttributes.addFlashAttribute("errorMessage", "Danh mục không tồn tại!");
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Xóa danh mục thất bại: " + e.getMessage());
        }
        return "redirect:/categories";
    }

    // Tìm kiếm danh mục theo tên
    @GetMapping("/search")
    public String searchCategories(@RequestParam("name") String name, Model model) {
        List<Category> categories = categoryService.getAllCategories();
        model.addAttribute("categories", categories);
        model.addAttribute("searchTerm", name);
        return "category/list";
    }
}
