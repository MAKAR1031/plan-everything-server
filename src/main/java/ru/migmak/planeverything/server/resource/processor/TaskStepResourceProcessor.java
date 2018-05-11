package ru.migmak.planeverything.server.resource.processor;

import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.ResourceProcessor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.migmak.planeverything.server.controller.TaskStepsController;
import ru.migmak.planeverything.server.domain.Account;
import ru.migmak.planeverything.server.domain.ProjectMember;
import ru.migmak.planeverything.server.domain.Task;
import ru.migmak.planeverything.server.domain.TaskStep;
import ru.migmak.planeverything.server.exception.BadRequestException;
import ru.migmak.planeverything.server.service.AccountService;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;


@Component
@Transactional
@RequiredArgsConstructor
public class TaskStepResourceProcessor implements ResourceProcessor<Resource<TaskStep>> {

    private final AccountService accountService;

    @Override
    public Resource<TaskStep> process(Resource<TaskStep> resource) {
        TaskStep step = resource.getContent();
        Task task = step.getTask();
        Account currentAccount = accountService.getCurrentAccount();
        ProjectMember currentMember = task.getProject()
                .getMembers()
                .stream()
                .filter(m -> m.getAccount().getId().equals(currentAccount.getId()))
                .findFirst()
                .orElseThrow(() -> new BadRequestException("User is not a member of the project"));
        if (task.getStatus().isInProgress() &&
                task.getAssignee() != null &&
                task.getAssignee().getId().equals(currentMember.getId())) {
            resource.add(linkTo(methodOn(TaskStepsController.class)
                    .complete(step.getId(), null, null))
                    .withRel("complete"));
        }
        return resource;
    }
}
