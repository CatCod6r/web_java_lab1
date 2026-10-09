package com.example.lab1.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = CosmicWordValidator.class)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface CosmicWordCheck {
    String message() default "Name must contain a cosmic word (e.g., star, galaxy, comet, nebula, moon)";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}