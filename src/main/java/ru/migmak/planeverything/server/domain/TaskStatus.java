package ru.migmak.planeverything.server.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

import static ru.migmak.planeverything.server.domain.enums.TaskStatusCode.*;

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
    @JsonIgnore
    private String code;

    @JsonIgnore
    public boolean isEditable() {
        return !CREATED.name().equals(code);
    }

    @JsonIgnore
    public boolean isFulfilled() {
        return FULFILLED.name().equals(code);
    }

    @JsonIgnore
    public boolean isInProgress() {
        return IN_PROGRESS.name().equals(code);
    }
}
