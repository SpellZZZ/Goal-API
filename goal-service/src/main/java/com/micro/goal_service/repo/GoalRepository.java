package com.micro.goal_service.repo;

import com.micro.goal_service.model.Goal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface GoalRepository extends JpaRepository<Goal, Long>, JpaSpecificationExecutor<Goal> {
}
