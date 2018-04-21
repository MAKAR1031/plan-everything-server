package ru.migmak.planeverything.server.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "task_events")
public class TaskEvent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 200)
    private String name;

    @Column(name = "event_time", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date time;

    @ManyToOne
    @JoinColumn(name = "event_type_id", nullable = false)
    @JsonIgnore
    private EventType type;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "task_id", nullable = false)
    private Task task;

    @ManyToOne
    @JoinColumn(name = "initiator_id", nullable = false)
    private Account initiator;

    public TaskEvent(String name, EventType type, Task task, Account initiator) {
        this.time = new Date();
        this.name = name;
        this.type = type;
        this.task = task;
        this.initiator = initiator;
    }

    @SuppressWarnings("unused")
    public String getEventType() {
        return this.type.getCode();
    }
}
