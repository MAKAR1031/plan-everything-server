package ru.migmak.planeverything.server.resource.processor;

import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.ResourceProcessor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.migmak.planeverything.server.controller.TasksController;
import ru.migmak.planeverything.server.domain.Account;
import ru.migmak.planeverything.server.domain.ProjectMember;
import ru.migmak.planeverything.server.domain.Task;
import ru.migmak.planeverything.server.repository.AccountRepository;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;
import static ru.migmak.planeverything.server.domain.enums.PrivilegeCode.MANAGE_TASKS;
import static ru.migmak.planeverything.server.domain.enums.TaskStatusCode.*;

@Component
@Transactional
@RequiredArgsConstructor
public class TaskResourceProcessor implements ResourceProcessor<Resource<Task>> {

    private final AccountRepository accountRepository;

    @Override
    public Resource<Task> process(Resource<Task> resource) {
        Task task = resource.getContent();
        Account account = accountRepository.findCurrentAccount().orElse(null);
        if (account == null) {
            return resource;
        }
        ProjectMember member = task.getProject().findMemberByAccountId(account.getId());
        if (member == null) {
            return resource;
        }
        String statusCode = task.getStatus().getCode();
        if ((statusCode.equals(CREATED.name()) || statusCode.equals(ASSIGNED.name())) && member.hasPrivilege(MANAGE_TASKS)) {
            resource.add(linkTo(methodOn(TasksController.class).assign(task.getId(), null, null)).withRel("assign"));
        }
        if (!task.getAssignee().getAccount().getId().equals(account.getId())) {
            return resource;
        }
        if (statusCode.equals(ASSIGNED.name())) {
            resource.add(linkTo(methodOn(TasksController.class).start(task.getId(), null)).withRel("start"));
        }
        if (statusCode.equals(FULFILLED.name())) {
            resource.add(linkTo(methodOn(TasksController.class).estimate(task.getId(), null)).withRel("estimate"));
        }
        return resource;
    }
}
