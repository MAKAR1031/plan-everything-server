package ru.migmak.planeverything.server.resource.processor.task;

import org.springframework.data.rest.webmvc.support.RepositoryEntityLinks;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.ResourceProcessor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.migmak.planeverything.server.domain.Task;
import ru.migmak.planeverything.server.repository.AccountRepository;

@Component
@Transactional
public class TaskResourceProcessor extends AbstractTaskResourceProcessor implements ResourceProcessor<Resource<Task>> {

    public TaskResourceProcessor(AccountRepository accountRepository, RepositoryEntityLinks links) {
        super(accountRepository, links);
    }

    @Override
    public Resource<Task> process(Resource<Task> resource) {
        Task task = resource.getContent();
        processTaskResource(resource, task);
        return resource;
    }
}
