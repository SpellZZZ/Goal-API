package com.micro.goal_service.controller;

import com.micro.goal_service.dto.PeriodDto;
import com.micro.goal_service.handler.annotation.MinValueID;
import com.micro.goal_service.service.PeriodService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Validated
@RestController
@RequestMapping("/api/period")
@RequiredArgsConstructor
public class PeriodController {

    private final PeriodService periodService;

    @GetMapping
    @PreAuthorize("hasAnyAuthority('user')")
    public List<PeriodDto> getPeriods() {
        log.info("Getting periods " + LocalDateTime.now());
        return periodService.getPeriodsDto();
    }

    @GetMapping("{id}")
    @PreAuthorize("hasAnyAuthority('user')")
    public PeriodDto getPeriod(@MinValueID @PathVariable Long id) {
        log.info("Getting period " + LocalDateTime.now());
        return periodService.getPeriodDtoById(id);
    }
}
