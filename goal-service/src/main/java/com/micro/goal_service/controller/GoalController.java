package com.micro.goal_service.controller;

import com.micro.goal_service.dto.GoalCreateDto;
import com.micro.goal_service.dto.GoalDto;
import com.micro.goal_service.dto.GoalUpdateDto;
import com.micro.goal_service.filter.GoalFilter;
import com.micro.goal_service.handler.annotation.MinValueID;
import com.micro.goal_service.service.GoalService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.SortDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@Slf4j
@Validated
@RestController
@RequestMapping("/api/goal")
@RequiredArgsConstructor
public class GoalController {

    private final GoalService goalService;
    private static final String DEFAULT_SORT_FIELD = "id";
    private static final int DEFAULT_PAGE_SIZE = 20;

    @PostMapping
    @PreAuthorize("hasAnyAuthority('user')")
    public GoalDto createGoal(@Valid @RequestBody GoalCreateDto goalCreateDto) {
        log.info("Creating goal " + LocalDateTime.now());
        return goalService.createGoal(goalCreateDto);
    }

    @GetMapping
    @PreAuthorize("hasAnyAuthority('user')")
    public Page<GoalDto> getGoals(
            @SortDefault(sort = DEFAULT_SORT_FIELD)
            @PageableDefault(size = DEFAULT_PAGE_SIZE) Pageable pageable
    ) {
        log.info("Getting goals " + LocalDateTime.now());
        return goalService.getGoalsDto(pageable);
    }

    @GetMapping("{id}")
    @PreAuthorize("hasAnyAuthority('user')")
    public GoalDto getGoal(@MinValueID @PathVariable Long id) {
        log.info("Getting goal " + LocalDateTime.now());
        return goalService.getGoalDtoById(id);
    }

    @GetMapping("filter")
    @PreAuthorize("hasAnyAuthority('user')")
    public Page<GoalDto> getFilteredGoals(
            @ModelAttribute GoalFilter goalFilter,
            @SortDefault(sort = DEFAULT_SORT_FIELD)
            @PageableDefault(size = DEFAULT_PAGE_SIZE) Pageable pageable
    ) {
        log.info("Getting filtered goals " + LocalDateTime.now());
        return goalService
                .getGoalsFilteredDto(goalFilter, pageable);
    }

    @DeleteMapping("{id}")
    @PreAuthorize("hasAnyAuthority('user')")
    public void deleteGoal(@MinValueID @PathVariable Long id) {
        log.info("Deleting goal " + LocalDateTime.now());
        goalService.deleteGoalById(id);
    }

    @PutMapping("{id}")
    @PreAuthorize("hasAnyAuthority('user')")
    public void putGoal(@MinValueID @PathVariable Long id,
                        @Valid @RequestBody GoalUpdateDto goalUpdateDto) {
        log.info("Updating goal " + LocalDateTime.now());
        goalService.updateGoal(id, goalUpdateDto);
    }

}
