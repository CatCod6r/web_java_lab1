package com.example.lab1.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.List;

public class CosmicWordValidator implements ConstraintValidator<CosmicWordCheck, String> {

    private static final List<String> COSMIC_KEYWORDS = List.of(
            "star", "galaxy", "comet", "nebula", "moon", "cosmo", "solar", "orbit"
    );

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null || value.trim().isEmpty()) {
            return true; // Let @NotBlank handle empty strings if needed
        }
        String lowerCaseValue = value.toLowerCase();
        return COSMIC_KEYWORDS.stream().anyMatch(lowerCaseValue::contains);
    }
}