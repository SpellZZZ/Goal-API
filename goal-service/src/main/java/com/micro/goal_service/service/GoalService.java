package com.micro.goal_service.service;

import com.micro.goal_service.dto.GoalCreateDto;
import com.micro.goal_service.dto.GoalDto;
import com.micro.goal_service.dto.GoalUpdateDto;
import com.micro.goal_service.exception.GoalException;
import com.micro.goal_service.filter.GoalFilter;
import com.micro.goal_service.model.Goal;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

public interface GoalService {
    GoalDto createGoal(GoalCreateDto goalCreateDto);
    Goal getGoalById(Long id) throws GoalException;
    GoalDto getGoalDtoById(Long id);
    Page<Goal> getGoals(Pageable pageable);
    Page<Goal> getGoalsWithSpecification(Pageable pageable, Specification<Goal> goalSpecification);
    Page<GoalDto> getGoalsDto(Pageable pageable);
    List<Goal> getGoalsByIds(List<Long> goalIds);
    void updateGoal(Long id, GoalUpdateDto goalUpdateDto);
    void deleteGoalById(Long id);
    Page<GoalDto> getGoalsFilteredDto(GoalFilter goalFilter, Pageable pageable);
}
