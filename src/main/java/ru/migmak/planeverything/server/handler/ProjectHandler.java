package ru.migmak.planeverything.server.handler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.rest.core.annotation.HandleBeforeCreate;
import org.springframework.data.rest.core.annotation.RepositoryEventHandler;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.migmak.planeverything.server.domain.Account;
import ru.migmak.planeverything.server.domain.Project;
import ru.migmak.planeverything.server.domain.ProjectMember;
import ru.migmak.planeverything.server.exception.ServiceException;
import ru.migmak.planeverything.server.repository.AccountRepository;
import ru.migmak.planeverything.server.repository.MemberRoleRepository;

import java.util.Date;

import static ru.migmak.planeverything.server.domain.enums.MemberRoleCode.PROJECT_MANAGER;

@RepositoryEventHandler
@Component
@Slf4j
@RequiredArgsConstructor
public class ProjectHandler {

    private final AccountRepository accountRepository;
    private final MemberRoleRepository memberRoleRepository;

    @HandleBeforeCreate
    @Transactional
    public void handleProjectCreate(Project project) {
        log.info("Create new project");
        project.setCreateDate(new Date());
        project.setOpened(true);
        Account author = accountRepository
                .findCurrentAccount()
                .orElseThrow(() -> new ServiceException("Current account not found"));
        project.setAuthor(author);
        ProjectMember member = new ProjectMember();
        member.setAccount(author);
        member.setProject(project);
        member.setRole(memberRoleRepository
                .findByCode(PROJECT_MANAGER.name())
                .orElseThrow(() -> new ServiceException("Cannot find project manager role")));
        project.addMember(member);
    }
}
