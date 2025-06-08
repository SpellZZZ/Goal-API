package com.micro.goal_service.mapper;

import com.micro.goal_service.dto.PriorityDto;
import com.micro.goal_service.model.Priority;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PriorityMapperService {

    public List<PriorityDto> mapPriorityListToPriorityDtoList(List<Priority> priorities) {
        return priorities.stream().map(this::mapPriorityToPriorityDto).toList();
    }

    public PriorityDto mapPriorityToPriorityDto(Priority priority) {
        return  PriorityDto.builder()
                .id(priority.getId())
                .priorityName(priority.getPriorityName())
                .build();
    }
}
