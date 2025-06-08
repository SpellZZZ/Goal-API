package com.micro.goal_service.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
public class DateManager {
    private final DateTimeFormatter formatter;

    DateManager(@Value("${pattern.date}") String pattern) {
        formatter = DateTimeFormatter.ofPattern(pattern);
    }

    public LocalDateTime stringToDate(String date) {
        return LocalDateTime.parse(date, formatter);
    }

    public LocalDateTime dateFormat(LocalDateTime date) {
        String formatedString = date.format(formatter);
        return LocalDateTime.parse(formatedString, formatter);
    }
}
