package com.micro.goal_service.service;

import com.micro.goal_service.dto.PriorityDto;
import com.micro.goal_service.exception.PriorityException;
import com.micro.goal_service.model.Priority;
import com.micro.goal_service.repo.PriorityRepository;
import com.micro.goal_service.mapper.PriorityMapperService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PriorityServiceImpl implements PriorityService {

    private final PriorityRepository priorityRepository;
    private final PriorityMapperService priorityMapperService;

    @Override
    public Priority getPriorityById(Long id) throws PriorityException {
        return priorityRepository.findById(id)
                .orElseThrow(() -> new PriorityException("Priority doesn't exist"));
    }

    @Override
    public PriorityDto getPriorityDtoById(Long id) {
        return priorityMapperService.mapPriorityToPriorityDto(
                getPriorityById(id)
        );
    }

    @Override
    public List<Priority> getPriorities() {
        return priorityRepository.findAll();
    }

    @Override
    public List<PriorityDto> getPrioritiesDto() {
        return priorityMapperService.mapPriorityListToPriorityDtoList(
                getPriorities()
        );
    }
}
