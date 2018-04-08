package ru.migmak.planeverything.server.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.migmak.planeverything.server.domain.Project;
import ru.migmak.planeverything.server.exception.NotFoundException;
import ru.migmak.planeverything.server.exception.ProjectAlreadyClosedException;
import ru.migmak.planeverything.server.repository.AccountRepository;
import ru.migmak.planeverything.server.repository.ProjectRepository;

@Service
@Transactional
public class ProjectsService {
    private ProjectRepository projectRepository;
    private AccountRepository accountRepository;

    @Autowired
    public ProjectsService(ProjectRepository projectRepository, AccountRepository accountRepository) {
        this.projectRepository = projectRepository;
        this.accountRepository = accountRepository;
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
}
