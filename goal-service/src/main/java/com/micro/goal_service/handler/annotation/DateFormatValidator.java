package com.micro.goal_service.handler.annotation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Value;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class DateFormatValidator implements ConstraintValidator<DateFormat, String> {

    @Value("${pattern.date}")
    private String datePattern;

    @Override
    public boolean isValid(String date, ConstraintValidatorContext context) {
        if (date == null) {
            return false;
        }

        try {
            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(datePattern);
            LocalDateTime.parse(date, dateTimeFormatter);
            return true;
        } catch (DateTimeParseException dateTimeParseException) {
            return false;
        }
    }
}
