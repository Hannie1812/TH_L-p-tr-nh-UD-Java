package com.nbhang.controllers;

import com.nbhang.services.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
@RequiredArgsConstructor
public class GlobalControllerAdvice {

    private final CategoryService categoryService;

    @ModelAttribute
    public void addAttributes(Model model) {
        model.addAttribute("categories", categoryService.getAllCategories());
    }
}
