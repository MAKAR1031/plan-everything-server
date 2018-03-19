package ru.migmak.planeverything.server.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "task_steps")
public class TaskStep {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private boolean completed;

    @Column(nullable = false)
    private boolean needReport;

    @Lob
    private String report;

    @ManyToOne
    @JoinColumn(name = "task_id", nullable = false)
    private Task task;
}
