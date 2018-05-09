package ru.migmak.planeverything.server.resource.processor.task;

import lombok.RequiredArgsConstructor;
import org.springframework.data.rest.webmvc.support.RepositoryEntityLinks;
import org.springframework.hateoas.Resource;
import ru.migmak.planeverything.server.controller.TasksController;
import ru.migmak.planeverything.server.domain.Account;
import ru.migmak.planeverything.server.domain.ProjectMember;
import ru.migmak.planeverything.server.domain.Task;
import ru.migmak.planeverything.server.repository.AccountRepository;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;
import static ru.migmak.planeverything.server.domain.enums.PrivilegeCode.MANAGE_TASKS;
import static ru.migmak.planeverything.server.domain.enums.TaskStatusCode.*;

@RequiredArgsConstructor
public abstract class AbstractTaskResourceProcessor {

    private final AccountRepository accountRepository;
    private final RepositoryEntityLinks links;

    protected void processTaskResource(Resource<?> resource, Task task) {
        Account account = accountRepository.findCurrentAccount().orElse(null);
        if (account == null) {
            return;
        }
        ProjectMember member = task.getProject().findMemberByAccountId(account.getId());
        if (member == null) {
            return;
        }
        String statusCode = task.getStatus().getCode();
        if (statusCode.equals(CREATED.name()) && member.hasPrivilege(MANAGE_TASKS)) {
            resource.add(links.linkToSingleResource(Task.class, task.getId()).withRel("edit"));
        }
        if ((statusCode.equals(CREATED.name()) || statusCode.equals(ASSIGNED.name())) &&
                member.hasPrivilege(MANAGE_TASKS)) {
            resource.add(linkTo(methodOn(TasksController.class).assign(task.getId(), null, null)).withRel("assign"));
        }
        ProjectMember assignee = task.getAssignee();
        if (assignee == null || !assignee.getAccount().getId().equals(account.getId())) {
            return;
        }
        if (statusCode.equals(ASSIGNED.name())) {
            resource.add(linkTo(methodOn(TasksController.class).start(task.getId(), null)).withRel("start"));
        }
        if (statusCode.equals(FULFILLED.name())) {
            resource.add(linkTo(methodOn(TasksController.class).estimate(task.getId(), null)).withRel("estimate"));
        }
    }
}
