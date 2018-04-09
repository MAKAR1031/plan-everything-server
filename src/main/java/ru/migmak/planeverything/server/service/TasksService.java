package ru.migmak.planeverything.server.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.migmak.planeverything.server.domain.*;
import ru.migmak.planeverything.server.domain.enums.TaskStatusCode;
import ru.migmak.planeverything.server.exception.BadRequestException;
import ru.migmak.planeverything.server.exception.NotFoundException;
import ru.migmak.planeverything.server.exception.ServiceException;
import ru.migmak.planeverything.server.repository.AccountRepository;
import ru.migmak.planeverything.server.repository.EventTypeRepository;
import ru.migmak.planeverything.server.repository.TaskRepository;
import ru.migmak.planeverything.server.repository.TaskStatusRepository;

import static ru.migmak.planeverything.server.domain.enums.EventTypeCode.UPDATE;
import static ru.migmak.planeverything.server.domain.enums.TaskStatusCode.ASSIGNED;

@Service
@Transactional
@RequiredArgsConstructor
public class TasksService {
    private final AccountRepository accountRepository;
    private final TaskRepository taskRepository;
    private final TaskStatusRepository statusRepository;
    private final EventTypeRepository eventTypeRepository;

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
