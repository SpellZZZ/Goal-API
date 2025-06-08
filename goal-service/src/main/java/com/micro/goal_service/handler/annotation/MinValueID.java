package com.micro.goal_service.handler.annotation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.PARAMETER, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = MinValueIDValidator.class)
public @interface MinValueID {
    String message() default "ID should be equal or greater 0";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
