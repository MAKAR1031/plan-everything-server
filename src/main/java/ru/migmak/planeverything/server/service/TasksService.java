package ru.migmak.planeverything.server.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.migmak.planeverything.server.domain.*;
import ru.migmak.planeverything.server.domain.enums.EventTypeCode;
import ru.migmak.planeverything.server.exception.BadRequestException;
import ru.migmak.planeverything.server.exception.NotFoundException;
import ru.migmak.planeverything.server.exception.ServiceException;
import ru.migmak.planeverything.server.repository.AccountRepository;
import ru.migmak.planeverything.server.repository.EventTypeRepository;
import ru.migmak.planeverything.server.repository.TaskRepository;
import ru.migmak.planeverything.server.repository.TaskStatusRepository;

import static ru.migmak.planeverything.server.domain.enums.TaskStatusCode.ASSIGNED;

@Service
public class TasksService {
    private final AccountRepository accountRepository;
    private final TaskRepository taskRepository;
    private final TaskStatusRepository statusRepository;
    private final EventTypeRepository eventTypeRepository;

    @Autowired
    public TasksService(
            AccountRepository accountRepository,
            TaskRepository taskRepository,
            TaskStatusRepository statusRepository,
            EventTypeRepository eventTypeRepository
    ) {
        this.accountRepository = accountRepository;
        this.taskRepository = taskRepository;
        this.statusRepository = statusRepository;
        this.eventTypeRepository = eventTypeRepository;
    }

    @Transactional
    public Task assign(Long taskId, Long memberId) {
        Task task = taskRepository.findById(taskId).orElseThrow(NotFoundException::new);
        ProjectMember assignee = task.getProject()
                .getMembers()
                .stream()
                .filter(member -> memberId.equals(member.getId()))
                .findAny()
                .orElseThrow(() -> new BadRequestException("The member is not a member of this project"));
        task.setAssignee(assignee);
        TaskStatus assignedStatus = statusRepository.findByCode(ASSIGNED.name())
                .orElseThrow(() -> new ServiceException("Status 'ASSIGNED' not found"));
        task.setStatus(assignedStatus);
        EventType eventType = eventTypeRepository.findByCode(EventTypeCode.UPDATE.name())
                .orElseThrow(() -> new ServiceException("Event type 'UPDATE' not found"));
        Account currentAccount = accountRepository.findCurrentAccount()
                .orElseThrow(() -> new ServiceException("Current account not found"));
        ProjectMember initiator = task.getProject().getMembers()
                .stream()
                .filter(member -> member.getAccount().getId().equals(currentAccount.getId()))
                .findAny()
                .orElseThrow(() -> new BadRequestException("User is not a member of the project"));
        TaskEvent event = new TaskEvent("Task assigned", eventType, task, initiator);
        task.addEvent(event);
        return taskRepository.save(task);
    }
}
