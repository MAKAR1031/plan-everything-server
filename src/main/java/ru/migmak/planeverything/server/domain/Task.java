package ru.migmak.planeverything.server.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "tasks")
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 120)
    private String name;

    private String description;

    @ManyToOne
    @JoinColumn(name = "task_state_id", nullable = false)
    private TaskStatus status;

    @ManyToOne
    @JoinColumn(name = "member_author_id", nullable = false)
    private ProjectMember author;

    @ManyToOne
    @JoinColumn(name = "member_assignee_id")
    private ProjectMember assignee;

    @ManyToOne
    @JoinColumn(name = "project_id", nullable = false)
    private Project project;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "task")
    private List<TaskStep> steps;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "task")
    private List<TaskEvent> events;

    @ManyToMany(mappedBy = "project")
    private List<Tag> tags;

    public void addEvent(TaskEvent event) {
        if (events == null) {
            events = new ArrayList<>();
        }
        events.add(event);
    }
}
