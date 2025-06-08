package com.micro.goal_service.handler.annotation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class LengthBetweenValidator implements ConstraintValidator<LengthBetween, String> {

    private long minLength;
    private long maxLength;

    @Override
    public void initialize(LengthBetween constraintAnnotation) {
        minLength = constraintAnnotation.minLength();
        maxLength = constraintAnnotation.maxLength();
    }

    @Override
    public boolean isValid(String word, ConstraintValidatorContext context) {
        if (word == null) {
            return false;
        }
        long size = word.length();

        return minLength <= size && size <= maxLength;
    }
}
