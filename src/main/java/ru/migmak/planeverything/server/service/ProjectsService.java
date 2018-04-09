package ru.migmak.planeverything.server.service;

import lombok.RequiredArgsConstructor;
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
@RequiredArgsConstructor
public class ProjectsService {

    private final ProjectRepository projectRepository;
    private final AccountRepository accountRepository;

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
