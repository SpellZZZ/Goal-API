package com.micro.goal_service.controller;

import com.micro.goal_service.dto.PriorityDto;
import com.micro.goal_service.handler.annotation.MinValueID;
import com.micro.goal_service.service.PriorityService;
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
@RequestMapping("/api/priority")
@RequiredArgsConstructor
public class PriorityController {

    private final PriorityService priorityService;

    @GetMapping
    @PreAuthorize("hasAnyAuthority('user')")
    public List<PriorityDto> get() {
        log.info("Getting priorities " + LocalDateTime.now());
        return priorityService.getPrioritiesDto();
    }

    @GetMapping("{id}")
    @PreAuthorize("hasAnyAuthority('user')")
    public PriorityDto getPeriod(@MinValueID @PathVariable Long id) {
        log.info("Getting priority " + LocalDateTime.now());
        return priorityService.getPriorityDtoById(id);
    }
}
