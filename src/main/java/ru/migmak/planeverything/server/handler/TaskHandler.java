package ru.migmak.planeverything.server.handler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.rest.core.annotation.HandleBeforeCreate;
import org.springframework.data.rest.core.annotation.HandleBeforeDelete;
import org.springframework.data.rest.core.annotation.HandleBeforeSave;
import org.springframework.data.rest.core.annotation.RepositoryEventHandler;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.migmak.planeverything.server.domain.*;
import ru.migmak.planeverything.server.domain.enums.EventTypeCode;
import ru.migmak.planeverything.server.exception.BadRequestException;
import ru.migmak.planeverything.server.exception.ServiceException;
import ru.migmak.planeverything.server.repository.AccountRepository;
import ru.migmak.planeverything.server.repository.EventTypeRepository;
import ru.migmak.planeverything.server.repository.TaskStatusRepository;

import static ru.migmak.planeverything.server.domain.enums.EventTypeCode.*;
import static ru.migmak.planeverything.server.domain.enums.PrivilegeCode.MANAGE_TASKS;
import static ru.migmak.planeverything.server.domain.enums.TaskStatusCode.CREATED;

@Component
@RepositoryEventHandler
@Slf4j
@RequiredArgsConstructor
public class TaskHandler {

    private final AccountRepository accountRepository;
    private final TaskStatusRepository statusRepository;
    private final EventTypeRepository eventTypeRepository;

    @HandleBeforeCreate
    @Transactional
    public void handleCreate(Task task) {
        log.info("Create new task");
        Project project = task.getProject();
        if (project == null) {
            return;
        }
        TaskStatus createdStatus = statusRepository.findByCode(CREATED.name())
                .orElseThrow(() -> new ServiceException("Status 'CREATE' not found"));
        task.setStatus(createdStatus);
        ProjectMember author = getEventInitiator(project);
        if (!author.hasPrivilege(MANAGE_TASKS)) {
            throw new BadRequestException("You can not manage tasks");
        }
        task.setAuthor(author);
        TaskEvent event = new TaskEvent("Created", getEventType(CREATE), task, author);
        task.addEvent(event);
    }

    @HandleBeforeSave
    public void handleUpdate(Task task) {
        checkEditableStatus(task);
        ProjectMember initiator = getEventInitiator(task.getProject());
        TaskEvent event = new TaskEvent("Task updated", getEventType(UPDATE), task, initiator);
        task.addEvent(event);
    }

    @HandleBeforeDelete
    public void handleDelete(Task task) {
        checkEditableStatus(task);
        ProjectMember initiator = getEventInitiator(task.getProject());
        TaskEvent event = new TaskEvent("Task removed", getEventType(FINISH), task, initiator);
        task.addEvent(event);
    }

    private EventType getEventType(EventTypeCode code) {
        return eventTypeRepository.findByCode(code.name())
                .orElseThrow(() -> new ServiceException(String.format("Event type '%s' not found", code.name())));
    }

    private ProjectMember getEventInitiator(Project project) {
        Account currentAccount = accountRepository.findCurrentAccount()
                .orElseThrow(() -> new ServiceException("Current account not found"));
        return project.getMembers()
                .stream()
                .filter(member -> member.getAccount().getId().equals(currentAccount.getId()))
                .findAny()
                .orElseThrow(() -> new BadRequestException("User is not a member of the project"));
    }

    private void checkEditableStatus(Task task) {
        if (!task.getStatus().isCreated()) {
            throw new BadRequestException("The task can not be edited or deleted in this status");
        }
    }
}
