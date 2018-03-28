package ru.migmak.planeverything.server.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.migmak.planeverything.server.domain.Account;
import ru.migmak.planeverything.server.domain.Project;
import ru.migmak.planeverything.server.domain.ProjectMember;
import ru.migmak.planeverything.server.domain.enums.MemberRoleCode;
import ru.migmak.planeverything.server.exception.ProjectCreateException;
import ru.migmak.planeverything.server.repository.AccountRepository;
import ru.migmak.planeverything.server.repository.MemberRoleRepository;
import ru.migmak.planeverything.server.repository.ProjectRepository;
import ru.migmak.planeverything.server.ro.ProjectRo;

import java.util.Date;

@Service
public class ProjectsService {
    private ProjectRepository projectRepository;
    private AccountRepository accountRepository;
    private MemberRoleRepository memberRoleRepository;

    @Autowired
    public ProjectsService(ProjectRepository projectRepository,
                           AccountRepository accountRepository,
                           MemberRoleRepository memberRoleRepository) {
        this.projectRepository = projectRepository;
        this.accountRepository = accountRepository;
        this.memberRoleRepository = memberRoleRepository;
    }

    public Project create(ProjectRo projectRo) {
        Project project = new Project();
        project.setName(projectRo.getName());
        project.setDescription(projectRo.getDescription());
        project.setOpened(true);
        project.setCreateDate(new Date());
        Account author = accountRepository.findCurrentAccount().orElseThrow(ProjectCreateException::new);
        project.setAuthor(author);
        ProjectMember member = new ProjectMember();
        member.setAccount(author);
        member.setProject(project);
        member.setRole(memberRoleRepository.findByCode(MemberRoleCode.PROJECT_MANAGER.name())
                                           .orElseThrow(ProjectCreateException::new));
        project.addMember(member);
        return projectRepository.save(project);
    }
}
