package ru.migmak.planeverything.server.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.migmak.planeverything.server.domain.Account;
import ru.migmak.planeverything.server.domain.MemberRole;
import ru.migmak.planeverything.server.domain.Project;
import ru.migmak.planeverything.server.domain.ProjectMember;
import ru.migmak.planeverything.server.exception.MemberAlreadyConsistOfProject;
import ru.migmak.planeverything.server.exception.NotFoundException;
import ru.migmak.planeverything.server.exception.ProjectAlreadyClosedException;
import ru.migmak.planeverything.server.exception.ProjectCreateException;
import ru.migmak.planeverything.server.repository.AccountRepository;
import ru.migmak.planeverything.server.repository.MemberRoleRepository;
import ru.migmak.planeverything.server.repository.ProjectRepository;
import ru.migmak.planeverything.server.ro.ProjectRo;

import java.util.Date;
import java.util.List;

import static ru.migmak.planeverything.server.domain.enums.MemberRoleCode.PROJECT_MANAGER;

@Service
@Transactional
public class ProjectsService {
    private ProjectRepository projectRepository;
    private AccountRepository accountRepository;
    private MemberRoleRepository memberRoleRepository;

    @Autowired
    public ProjectsService(
            ProjectRepository projectRepository,
            AccountRepository accountRepository,
            MemberRoleRepository memberRoleRepository
    ) {
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
        member.setRole(memberRoleRepository
                .findByCode(PROJECT_MANAGER.name())
                .orElseThrow(ProjectCreateException::new));
        project.addMember(member);
        return projectRepository.save(project);
    }

    public Project close(Long id) {
        Project project = projectRepository.findById(id).orElseThrow(NotFoundException::new);
        accountRepository.findCurrentAccount().ifPresent(account -> {
            if (!account.getId().equals(project.getAuthor().getId())) {
                throw new AccessDeniedException("Account " + account.getId() + " is not author of project");
            }
        });
        if (!project.isOpened()) {
            throw new ProjectAlreadyClosedException();
        }
        project.setOpened(false);
        return projectRepository.save(project);
    }

    public void addMember(Long id, Long accountId, Long roleId) {
        Project project = projectRepository.findById(id).orElseThrow(NotFoundException::new);
        if (project.getMembers().stream().anyMatch(member -> member.getAccount().getId().equals(accountId))) {
            throw new MemberAlreadyConsistOfProject();
        }
        Account account = accountRepository.findById(accountId).orElseThrow(NotFoundException::new);
        MemberRole memberRole = memberRoleRepository.findById(roleId).orElseThrow(NotFoundException::new);
        ProjectMember member = new ProjectMember();
        member.setAccount(account);
        member.setRole(memberRole);
        member.setProject(project);
        project.addMember(member);
        projectRepository.save(project);
    }

    public void excludeMember(Long id, Long memberId) {
        Project project = projectRepository.findById(id).orElseThrow(NotFoundException::new);
        project.getMembers().removeIf(next -> next.getId().equals(memberId));
    }

    public void changeMemberRole(Long id, Long memberId, Long roleId) {
        Project project = projectRepository.findById(id).orElseThrow(NotFoundException::new);
        ProjectMember projectMember = project.getMembers()
                .stream()
                .filter(member -> member.getId().equals(memberId))
                .findFirst()
                .orElseThrow(NotFoundException::new);
        MemberRole memberRole = memberRoleRepository.findById(roleId).orElseThrow(NotFoundException::new);
        projectMember.setRole(memberRole);
        projectRepository.save(project);
    }
}
