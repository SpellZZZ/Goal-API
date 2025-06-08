package com.micro.goal_service.dto;

import com.micro.goal_service.model.GoalDescription;
import com.micro.goal_service.model.Payment;
import com.micro.goal_service.model.Period;
import com.micro.goal_service.model.Priority;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class GoalDto {
    private Long id;
    private String goalName;
    private LocalDateTime dateDeadline;
    private LocalDateTime dateStart;
    private LocalDateTime dateEnd;
    private BigDecimal currentMoney;
    private BigDecimal targetMoney;
    private boolean isCompleted;
    private boolean isFrozen;
    private boolean isArchived;
    private Priority priority;
    private Period period;
    private GoalDescription goalDescription;
    private List<Payment> payments;
}
