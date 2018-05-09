package ru.migmak.planeverything.server.resource.processor.task;

import org.springframework.data.rest.webmvc.support.RepositoryEntityLinks;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.ResourceProcessor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.migmak.planeverything.server.domain.projection.FullTaskProjection;
import ru.migmak.planeverything.server.repository.AccountRepository;
import ru.migmak.planeverything.server.repository.TaskRepository;

@Component
@Transactional
public class TaskFullProjectionResourceProcessor extends AbstractTaskResourceProcessor implements
        ResourceProcessor<Resource<FullTaskProjection>> {

    private final TaskRepository taskRepository;

    public TaskFullProjectionResourceProcessor(
            AccountRepository accountRepository,
            RepositoryEntityLinks links,
            TaskRepository taskRepository
    ) {
        super(accountRepository, links);
        this.taskRepository = taskRepository;
    }

    @Override
    public Resource<FullTaskProjection> process(Resource<FullTaskProjection> resource) {
        taskRepository.findById(resource.getContent().getId()).ifPresent(task -> processTaskResource(resource, task));
        return resource;
    }
}
