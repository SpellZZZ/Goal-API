package com.micro.goal_service.specification;

import com.micro.goal_service.model.Goal;
import com.micro.goal_service.model.Period;
import com.micro.goal_service.model.Priority;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class GoalSpecifications {
    public Specification<Goal> isCompleted(Boolean completed) {
        return (root, query, cb) -> completed == null ? null : cb.equal(root.get(Goal.Fields.isCompleted), completed);
    }
    public Specification<Goal> hasPriorityName(String priorityName) {
        return (root, query, cb) -> priorityName == null ? null
                : cb.equal(root.join(Goal.Fields.priority).get(Priority.Fields.priorityName), priorityName);
    }
    public Specification<Goal> hasPeriodName(String periodName) {
        return (root, query, cb) -> periodName == null ? null
                : cb.equal(root.join(Goal.Fields.period).get(Period.Fields.periodName), periodName);
    }
    public Specification<Goal> currentMoneyLessThan(BigDecimal money) {
        return (root, query, cb) -> money == null ? null : cb.lessThan(root.get(Goal.Fields.currentMoney), money);
    }
    public Specification<Goal> currentMoneyMoreThan(BigDecimal money) {
        return (root, query, cb) -> money == null ? null : cb.greaterThan(root.get(Goal.Fields.currentMoney), money);
    }
}
