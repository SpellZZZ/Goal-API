package com.micro.goal_service.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PeriodDto {
    private Long id;
    private String periodName;
}
