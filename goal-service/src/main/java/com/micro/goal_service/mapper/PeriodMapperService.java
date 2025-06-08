package com.micro.goal_service.mapper;

import com.micro.goal_service.dto.PeriodDto;
import com.micro.goal_service.model.Period;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PeriodMapperService {

    public List<PeriodDto> mapPeriodListToPeriodDtoList(List<Period> periods) {
        return periods.stream().map(this::mapPeriodToPeriodDto).toList();
    }

    public PeriodDto mapPeriodToPeriodDto(Period period) {
        return  PeriodDto.builder()
                .id(period.getId())
                .periodName(period.getPeriodName())
                .build();
    }
}
