package com.micro.goal_service.handler.annotation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class MinValueIDValidator implements ConstraintValidator<MinValueID, Long> {

    private static final Long MIN_ID_VALUE = 0L;

    @Override
    public boolean isValid(Long value, ConstraintValidatorContext context) {
        return value != null && MIN_ID_VALUE <= value;
    }
}
