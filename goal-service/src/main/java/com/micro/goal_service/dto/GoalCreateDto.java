package com.micro.goal_service.dto;

import com.micro.goal_service.handler.annotation.DateFormat;
import com.micro.goal_service.handler.annotation.LengthBetween;
import com.micro.goal_service.handler.annotation.MinValueID;
import jakarta.validation.constraints.Min;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class GoalCreateDto {
    @LengthBetween(
            minLength = 5,
            maxLength = 60,
            message = "Length should be between 5 and 60"
    )
    private String goalName;
    @DateFormat
    private String dateDeadline;
    @Min(
            value = 0,
            message = "Value should be above 0"
    )
    private BigDecimal currentMoney;
    @Min(
            value = 0,
            message = "Value should be above 0"
    )
    private BigDecimal targetMoney;
    @MinValueID
    private Long periodID;
    @MinValueID
    private Long priorityID;
    @LengthBetween(
            minLength = 0,
            maxLength = 200,
            message = "Length should be between 0 and 200"
    )
    private String goalDescription;
}
