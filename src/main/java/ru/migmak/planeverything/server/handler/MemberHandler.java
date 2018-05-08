package ru.migmak.planeverything.server.handler;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.rest.core.annotation.HandleBeforeCreate;
import org.springframework.data.rest.core.annotation.RepositoryEventHandler;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.migmak.planeverything.server.domain.MemberRole;
import ru.migmak.planeverything.server.domain.Project;
import ru.migmak.planeverything.server.domain.ProjectMember;
import ru.migmak.planeverything.server.domain.enums.MemberRoleCode;
import ru.migmak.planeverything.server.exception.BadRequestException;
import ru.migmak.planeverything.server.exception.ServiceException;
import ru.migmak.planeverything.server.repository.MemberRoleRepository;

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
}
