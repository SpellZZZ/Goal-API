package com.micro.goal_service.filter;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class GoalFilter {
    private Boolean completed;
    private String priorityName;
    private String periodName;
    private BigDecimal currentMoneyLessThan;
    private BigDecimal currentMoneyMoreThan;
}
