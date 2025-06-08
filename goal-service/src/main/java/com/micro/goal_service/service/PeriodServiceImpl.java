package com.micro.goal_service.service;

import com.micro.goal_service.dto.PeriodDto;
import com.micro.goal_service.exception.PeriodException;
import com.micro.goal_service.model.Period;
import com.micro.goal_service.repo.PeriodRepository;
import com.micro.goal_service.mapper.PeriodMapperService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PeriodServiceImpl implements PeriodService {

    private final PeriodRepository periodRepository;
    private final PeriodMapperService periodMapperService;

    @Override
    public Period getPeriodById(Long id) throws PeriodException {
        return periodRepository.findById(id)
                .orElseThrow(() -> new PeriodException("Period doesn't exist"));
    }

    @Override
    public PeriodDto getPeriodDtoById(Long id) {
        return periodMapperService.mapPeriodToPeriodDto(
                getPeriodById(id)
        );
    }

    @Override
    public List<Period> getPeriods() {
        return periodRepository.findAll();
    }

    @Override
    public List<PeriodDto> getPeriodsDto() {
        return periodMapperService.mapPeriodListToPeriodDtoList(
                getPeriods()
        );
    }
}
