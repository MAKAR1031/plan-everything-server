package ru.migmak.planeverything.server.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.migmak.planeverything.server.domain.*;
import ru.migmak.planeverything.server.domain.enums.TaskStatusCode;
import ru.migmak.planeverything.server.exception.BadRequestException;
import ru.migmak.planeverything.server.exception.NotFoundException;
import ru.migmak.planeverything.server.exception.ServiceException;
import ru.migmak.planeverything.server.repository.*;

import java.util.List;
import java.util.stream.Collectors;

import static ru.migmak.planeverything.server.domain.enums.EventTypeCode.UPDATE;
import static ru.migmak.planeverything.server.domain.enums.TaskStatusCode.ASSIGNED;
import static ru.migmak.planeverything.server.domain.enums.TaskStatusCode.COMPLETED;

@Service
@Transactional
@RequiredArgsConstructor
public class TasksService {
    private final AccountRepository accountRepository;
    private final TaskRepository taskRepository;
    private final TaskStatusRepository statusRepository;
    private final EventTypeRepository eventTypeRepository;
    private final TaskStepRepository taskStepRepository;

    public Task assign(Long id, Long memberId) {
        Task task = taskRepository.findById(id).orElseThrow(NotFoundException::new);
        ProjectMember assignee = task.getProject()
                .getMembers()
                .stream()
                .filter(member -> memberId.equals(member.getId()))
                .findAny()
                .orElseThrow(() -> new BadRequestException("The member is not a member of this project"));
        task.setAssignee(assignee);
        TaskStatus assignedStatus = getStatus(ASSIGNED);
        task.setStatus(assignedStatus);
        Account currentAccount = getCurrentAccount();
        ProjectMember initiator = task.getProject().getMembers()
                .stream()
                .filter(member -> member.getAccount().getId().equals(currentAccount.getId()))
                .findAny()
                .orElseThrow(() -> new BadRequestException("User is not a member of the project"));
        TaskEvent event = createUpdateEvent(task, initiator, "Task assigned");
        task.addEvent(event);
        return taskRepository.save(task);
    }

    public Task startTask(Long id) {
        Task task = taskRepository.findById(id).orElseThrow(NotFoundException::new);
        Account currentAccount = getCurrentAccount();
        ProjectMember initiator = task.getProject()
                .getMembers()
                .stream()
                .filter(member -> member.getAccount().getId().equals(currentAccount.getId()))
                .findAny()
                .orElseThrow(() -> new BadRequestException("Task not assigned to current user"));
        TaskEvent event = createUpdateEvent(task, initiator, "Task execution started");
        task.addEvent(event);
        TaskStatus status = getStatus(TaskStatusCode.IN_PROGRESS);
        task.setStatus(status);
        return taskRepository.save(task);
    }

    public TaskStep completeStep(Long id, String report) {
        TaskStep step = taskStepRepository.findById(id).orElseThrow(NotFoundException::new);
        Account currentAccount = getCurrentAccount();
        ProjectMember initiator = step.getTask().getProject()
                .getMembers()
                .stream()
                .filter(member -> member.getAccount().getId().equals(currentAccount.getId()))
                .findAny()
                .orElseThrow(() -> new BadRequestException("Task not assigned to current user"));
        TaskEvent event = createUpdateEvent(step.getTask(), initiator, "Step complete");
        step.getTask().addEvent(event);
        List<Long> stepsIds = step.getTask()
                .getSteps()
                .stream()
                .filter(s -> !s.isCompleted())
                .map(TaskStep::getId)
                .collect(Collectors.toList());
        if ((stepsIds.size() == 1) && stepsIds.get(0).equals(id)) {
            TaskStatus status = getStatus(COMPLETED);
            step.getTask().setStatus(status);
            TaskEvent completeEvent = createUpdateEvent(step.getTask(), initiator, "All steps complete");
            step.getTask().addEvent(completeEvent);
        }
        step.setCompleted(true);
        if (step.isNeedReport()) {
            if (report == null || report.isEmpty()) {
                throw new BadRequestException("A report should be submitted for this task");
            }
            step.setReport(report);
        }
        return taskStepRepository.save(step);
    }

    private TaskStatus getStatus(TaskStatusCode statusCode) {
        return statusRepository.findByCode(statusCode.name())
                .orElseThrow(() -> new ServiceException(String.format("Task state %s not found", statusCode.name())));
    }

    private TaskEvent createUpdateEvent(Task task, ProjectMember initiator, String name) {
        EventType eventType = eventTypeRepository.findByCode(UPDATE.name())
                .orElseThrow(() -> new ServiceException("Event type 'UPDATE' not found"));
        return new TaskEvent(name, eventType, task, initiator);
    }

    private Account getCurrentAccount() {
        return accountRepository.findCurrentAccount()
                .orElseThrow(() -> new ServiceException("Current account not found"));
    }
}
