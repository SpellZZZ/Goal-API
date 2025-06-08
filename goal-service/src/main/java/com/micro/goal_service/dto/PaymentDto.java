package com.micro.goal_service.dto;

import com.micro.goal_service.model.Goal;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class PaymentDto {
    private Long id;
    private BigDecimal paymentValue;
    private List<Goal> goals;
    private LocalDateTime paymentDate;
}
