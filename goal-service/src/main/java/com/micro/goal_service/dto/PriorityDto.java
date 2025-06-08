package com.micro.goal_service.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PriorityDto {
    private Long id;
    private String priorityName;
}
