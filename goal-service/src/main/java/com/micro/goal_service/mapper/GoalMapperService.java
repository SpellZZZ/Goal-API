package com.micro.goal_service.mapper;

import com.micro.goal_service.dto.GoalCreateDto;
import com.micro.goal_service.dto.GoalDto;
import com.micro.goal_service.model.Goal;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GoalMapperService {

    public Goal mapGoalCreateDtoToGoal(GoalCreateDto goalCreateDto) {
        return Goal.builder()
                .goalName(goalCreateDto.getGoalName())
                .currentMoney(goalCreateDto.getCurrentMoney())
                .targetMoney(goalCreateDto.getTargetMoney())
                .build();
    }

    public GoalDto mapGoalToGoalDto(Goal goal) {
        return GoalDto.builder()
                .id(goal.getId())
                .goalName(goal.getGoalName())
                .dateDeadline(goal.getDateDeadline())
                .dateStart(goal.getDateStart())
                .dateEnd(goal.getDateEnd())
                .currentMoney(goal.getCurrentMoney())
                .targetMoney(goal.getTargetMoney())
                .isCompleted(goal.isCompleted())
                .isFrozen(goal.isFrozen())
                .isArchived(goal.isArchived())
                .priority(goal.getPriority())
                .period(goal.getPeriod())
                .goalDescription(goal.getGoalDescription())
                .payments(goal.getPayments())
                .build();
    }

    public Page<GoalDto> mapGoalListToGoalDtoList(Page<Goal> goals) {
        List<GoalDto> mappedGoals = goals.stream().map(this::mapGoalToGoalDto).toList();
        return new PageImpl<>(mappedGoals);
    }
}
