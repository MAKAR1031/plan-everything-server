package ru.migmak.planeverything.server.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import ru.migmak.planeverything.server.domain.enums.TaskStatusCode;

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

    public boolean is(TaskStatusCode code) {
        return this.code.equals(code.name());
    }

    @JsonIgnore
    public boolean isEditable() {
        return !is(CREATED);
    }

    @JsonIgnore
    public boolean isFulfilled() {
        return is(FULFILLED);
    }

    @JsonIgnore
    public boolean isInProgress() {
        return is(IN_PROGRESS);
    }
}
