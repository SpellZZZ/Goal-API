package com.micro.goal_service.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldNameConstants;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldNameConstants
@Table(name = "goals")
public class Goal {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    private String goalName;
    private LocalDateTime dateStart;
    private LocalDateTime dateEnd;
    private LocalDateTime dateDeadline;
    private boolean isCompleted;
    private boolean isFrozen;
    private boolean isArchived;
    private BigDecimal currentMoney;
    private BigDecimal targetMoney;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "goal_description_id", referencedColumnName = "id")
    private GoalDescription goalDescription;

    @ManyToOne(fetch = FetchType.LAZY)
    private Period period;

    @ManyToOne(fetch = FetchType.LAZY)
    private Priority priority;

    @ManyToMany(mappedBy = "goals", fetch = FetchType.LAZY)
    @Builder.Default
    private List<Payment> payments = new ArrayList<>();

}
