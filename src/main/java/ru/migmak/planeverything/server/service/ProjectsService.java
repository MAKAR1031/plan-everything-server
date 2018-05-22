package ru.migmak.planeverything.server.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.rest.webmvc.support.RepositoryEntityLinks;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.migmak.planeverything.server.domain.*;
import ru.migmak.planeverything.server.exception.NotFoundException;
import ru.migmak.planeverything.server.exception.ProjectAlreadyClosedException;
import ru.migmak.planeverything.server.repository.AccountRepository;
import ru.migmak.planeverything.server.repository.ProjectRepository;
import ru.migmak.planeverything.server.resource.ProjectProgress;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class ProjectsService {

    private final ProjectRepository projectRepository;
    private final AccountRepository accountRepository;
    private final RepositoryEntityLinks links;

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

    public List<Long> accountMemberIds(Project project) {
        return project.getMembers()
                .stream()
                .map(ProjectMember::getAccount)
                .map(Account::getId)
                .collect(Collectors.toList());
    }

    public ProjectProgress getStatistics(Long id) {
        Project project = projectRepository.findById(id).orElseThrow(NotFoundException::new);
        long completedSteps = project.getTasks()
                .stream()
                .map(Task::getSteps)
                .flatMap(List::stream)
                .filter(TaskStep::isCompleted)
                .count();
        long totalSteps = project.getTasks()
                .stream()
                .map(Task::getSteps)
                .mapToLong(List::size)
                .sum();
        ProjectProgress statistics = new ProjectProgress(completedSteps, totalSteps);
        statistics.add(links.linkToSingleResource(Project.class, id).withRel("project"));
        return statistics;
    }
}
