package ru.migmak.planeverything.server.handler;


import lombok.extern.slf4j.Slf4j;
import org.springframework.data.rest.core.annotation.HandleBeforeCreate;
import org.springframework.data.rest.core.annotation.RepositoryEventHandler;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.migmak.planeverything.server.domain.Project;
import ru.migmak.planeverything.server.domain.ProjectMember;
import ru.migmak.planeverything.server.exception.BadRequestException;

@RepositoryEventHandler
@Component
@Slf4j
public class MemberHandler {

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
    }
}
