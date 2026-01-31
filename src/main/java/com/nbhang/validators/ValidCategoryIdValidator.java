package com.nbhang.validators;

import com.nbhang.entities.Category;
import com.nbhang.validators.annotations.ValidCategoryId;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class ValidCategoryIdValidator implements
        ConstraintValidator<ValidCategoryId, Category> {
    @Override
    public boolean isValid(Category category,
            ConstraintValidatorContext context) {
        return category != null && category.getId() != null;
    }
}
