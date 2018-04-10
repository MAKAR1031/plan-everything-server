package ru.migmak.planeverything.server.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

import static ru.migmak.planeverything.server.domain.enums.TaskStatusCode.CREATED;
import static ru.migmak.planeverything.server.domain.enums.TaskStatusCode.FULFILLED;

@Getter
@Setter
@Entity
@Table(name = "task_statuses")
public class TaskStatus {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 20)
    private String name;

    @Column(nullable = false, unique = true, length = 20)
    private String code;

    public boolean isEditable() {
        return !CREATED.name().equals(code);
    }

    public boolean isFulfilled() {
        return FULFILLED.name().equals(code);
    }
}
