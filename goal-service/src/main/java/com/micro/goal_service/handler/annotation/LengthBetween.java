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
@Constraint(validatedBy = LengthBetweenValidator.class)
public @interface LengthBetween {
    String message() default "Length should be between {min} and {max}";

    long minLength();
    long maxLength();

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
