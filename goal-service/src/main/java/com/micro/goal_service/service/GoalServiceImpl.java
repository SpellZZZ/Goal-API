package com.micro.goal_service.service;

import com.micro.goal_service.dto.GoalCreateDto;
import com.micro.goal_service.dto.GoalDto;
import com.micro.goal_service.dto.GoalUpdateDto;
import com.micro.goal_service.exception.GoalException;
import com.micro.goal_service.filter.GoalFilter;
import com.micro.goal_service.model.Goal;
import com.micro.goal_service.model.GoalDescription;
import com.micro.goal_service.model.Period;
import com.micro.goal_service.model.Priority;
import com.micro.goal_service.repo.GoalRepository;
import com.micro.goal_service.mapper.GoalMapperService;
import com.micro.goal_service.specification.GoalSpecifications;
import com.micro.goal_service.util.DateManager;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class GoalServiceImpl implements GoalService {

    private final GoalMapperService goalMapperService;
    private final GoalRepository goalRepository;
    private final GoalSpecifications goalSpecifications;

    private final PriorityService priorityService;
    private final PeriodService periodService;
    private final DateManager dateManager;

    @Override
    public GoalDto createGoal(GoalCreateDto goalCreateDto) {
        Goal goal = goalMapperService.mapGoalCreateDtoToGoal(goalCreateDto);
        GoalDescription goalDescription = new GoalDescription();
        goalDescription.setDescription(goalCreateDto.getGoalDescription());

        Period period = periodService.getPeriodById(goalCreateDto.getPeriodID());
        Priority priority = priorityService.getPriorityById(goalCreateDto.getPriorityID());

        LocalDateTime goalStartDate = dateManager.dateFormat(LocalDateTime.now());
        LocalDateTime goalDeadlineDate = dateManager.stringToDate(goalCreateDto.getDateDeadline());

        goal.setGoalDescription(goalDescription);
        goal.setPeriod(period);
        goal.setPriority(priority);
        goal.setDateStart(goalStartDate);
        goal.setDateDeadline(goalDeadlineDate);

        return goalMapperService.mapGoalToGoalDto(goalRepository.save(goal));
    }

    @Override
    public Goal getGoalById(Long id) throws GoalException {
        return goalRepository.findById(id)
                .orElseThrow(() -> new GoalException("Goal doesn't exist"));
    }

    @Override
    public GoalDto getGoalDtoById(Long id) {
        return goalMapperService.mapGoalToGoalDto(getGoalById(id));
    }

    @Override
    public Page<Goal> getGoals(Pageable pageable) {
        return goalRepository.findAll(pageable);
    }

    @Override
    public Page<GoalDto> getGoalsDto(Pageable pageable) {
        return goalMapperService.mapGoalListToGoalDtoList(
                getGoals(pageable)
        );
    }

    @Override
    public List<Goal> getGoalsByIds(List<Long> goalIds) {
        return goalRepository.findAllById(goalIds);
    }

    @Override
    public void updateGoal(Long id, GoalUpdateDto goalUpdateDto) {
        Optional<Goal> goalOpt = goalRepository.findById(id);

        if (goalOpt.isPresent()) {
            Period period = periodService.getPeriodById(goalUpdateDto.getPeriodID());
            Priority priority = priorityService.getPriorityById(goalUpdateDto.getPriorityID());

            LocalDateTime goalStartDate = dateManager.dateFormat(LocalDateTime.now());
            LocalDateTime goalDeadlineDate = dateManager.stringToDate(goalUpdateDto.getDateDeadline());

            Goal goal = goalOpt.get();
            goal.setGoalName(goalUpdateDto.getGoalName());
            goal.setDateDeadline(goalDeadlineDate);
            goal.setDateStart(goalStartDate);
            goal.setCurrentMoney(goalUpdateDto.getCurrentMoney());
            goal.setTargetMoney(goalUpdateDto.getTargetMoney());
            goal.setPriority(priority);
            goal.setPeriod(period);
            goal.getGoalDescription().setDescription(goalUpdateDto.getGoalDescription());
            goalRepository.save(goal);
        } else {
            throw new GoalException("Goal not found");
        }
    }

    @Override
    public void deleteGoalById(Long id) {
        goalRepository.deleteById(id);
    }

    @Override
    public Page<Goal> getGoalsWithSpecification(Pageable pageable, Specification<Goal> goalSpecification) {
        return goalRepository.findAll(goalSpecification, pageable);
    }

    @Override
    public Page<GoalDto> getGoalsFilteredDto(GoalFilter goalFilter, Pageable pageable) {
        Specification<Goal> goalSpecification = Specification
                .where(goalSpecifications.isCompleted(goalFilter.getCompleted()))
                .and(goalSpecifications.hasPeriodName(goalFilter.getPeriodName()))
                .and(goalSpecifications.hasPriorityName(goalFilter.getPriorityName()))
                .and(goalSpecifications.currentMoneyLessThan(goalFilter.getCurrentMoneyLessThan()))
                .and(goalSpecifications.currentMoneyMoreThan(goalFilter.getCurrentMoneyMoreThan()));

        return goalMapperService.mapGoalListToGoalDtoList(
                getGoalsWithSpecification(pageable, goalSpecification));
    }

}
