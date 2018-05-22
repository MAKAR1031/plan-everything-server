package ru.migmak.planeverything.server.handler;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.rest.core.annotation.HandleBeforeCreate;
import org.springframework.data.rest.core.annotation.HandleBeforeDelete;
import org.springframework.data.rest.core.annotation.RepositoryEventHandler;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.migmak.planeverything.server.domain.*;
import ru.migmak.planeverything.server.domain.enums.MemberRoleCode;
import ru.migmak.planeverything.server.exception.BadRequestException;
import ru.migmak.planeverything.server.exception.ServiceException;
import ru.migmak.planeverything.server.repository.MemberRoleRepository;

import static ru.migmak.planeverything.server.domain.enums.TaskStatusCode.*;

@RepositoryEventHandler
@Component
@Slf4j
@RequiredArgsConstructor
public class MemberHandler {

    private final MemberRoleRepository repository;

    @HandleBeforeCreate
    @Transactional
    public void handleCreate(ProjectMember member) {
        Project project = member.getProject();
        if (project == null) {
            return;
        }
        boolean alreadyContains = project
                .getMembers()
                .stream()
                .anyMatch(m -> member.getAccount().getId().equals(m.getAccount().getId()));
        if (alreadyContains) {
            throw new BadRequestException("User is already a member");
        }
        if (member.getRole() == null) {
            MemberRole defaultRole = repository.findByCode(MemberRoleCode.PROJECT_EXECUTOR.name())
                    .orElseThrow(() -> new ServiceException("Default member role not found"));
            member.setRole(defaultRole);
        }
    }

    @HandleBeforeDelete
    @Transactional
    public void handleDelete(ProjectMember member) {
        Project project = member.getProject();
        if (project == null) {
            return;
        }
        long memberTasks = project.getTasks()
                .stream()
                .filter(task -> task.getAssignee() != null)
                .filter(task -> member.getId().equals(task.getAssignee().getId()))
                .filter(this::filterByStatus)
                .count();
        if (memberTasks > 0) {
            throw new BadRequestException("Member has not yet completed his tasks");
        }
    }

    private boolean filterByStatus(Task task) {
        TaskStatus status = task.getStatus();
        return status.is(ASSIGNED) || status.is(IN_PROGRESS) || status.is(FULFILLED);
    }
}
