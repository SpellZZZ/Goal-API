package com.micro.goal_service.service;

import com.micro.goal_service.dto.PriorityDto;
import com.micro.goal_service.exception.PriorityException;
import com.micro.goal_service.model.Priority;

import java.util.List;

public interface PriorityService {
    Priority getPriorityById(Long id) throws PriorityException;
    PriorityDto getPriorityDtoById(Long id);
    List<Priority> getPriorities();
    List<PriorityDto> getPrioritiesDto();
}
