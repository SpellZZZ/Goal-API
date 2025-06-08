package com.micro.goal_service.dto;


import jakarta.validation.constraints.Min;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
public class PaymentUpdateDto {
    @Min(
            value = 0,
            message = "Value should be above 0"
    )
    private BigDecimal paymentValue;
    private List<Long> goalIDs;
}
