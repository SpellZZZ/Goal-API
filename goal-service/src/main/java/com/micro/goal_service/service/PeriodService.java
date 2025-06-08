package com.micro.goal_service.service;

import com.micro.goal_service.dto.PeriodDto;
import com.micro.goal_service.exception.PeriodException;
import com.micro.goal_service.model.Period;

import java.util.List;

public interface PeriodService {
    Period getPeriodById(Long id) throws PeriodException;
    PeriodDto getPeriodDtoById(Long id);
    List<Period> getPeriods();
    List<PeriodDto> getPeriodsDto();
}
