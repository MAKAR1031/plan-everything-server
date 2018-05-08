package ru.migmak.planeverything.server.resource.processor;

import lombok.RequiredArgsConstructor;
import org.springframework.data.rest.webmvc.support.RepositoryEntityLinks;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.ResourceProcessor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.migmak.planeverything.server.domain.Account;
import ru.migmak.planeverything.server.domain.Project;
import ru.migmak.planeverything.server.domain.ProjectMember;
import ru.migmak.planeverything.server.domain.enums.PrivilegeCode;
import ru.migmak.planeverything.server.exception.BadRequestException;
import ru.migmak.planeverything.server.service.AccountService;

@Component
@Transactional
@RequiredArgsConstructor
public class ProjectResourceProcessor implements ResourceProcessor<Resource<Project>> {

    private static final String MANAGE_TASKS = "manageTasks";
    private static final String MANAGE_TAGS = "manageTags";
    private static final String MANAGE_MEMBERS = "manageMembers";
    private static final String MANAGE_HREF = "manage";
    private final AccountService accountService;
    private final RepositoryEntityLinks links;

    @Override
    public Resource<Project> process(Resource<Project> resource) {
        final Project project = resource.getContent();
        Account currentAccount = accountService.getCurrentAccount();
        project.getMembers().stream()
                .filter(m -> m.getAccount().getId().equals(currentAccount.getId()))
                .findFirst()
                .ifPresent(m -> resource.add(createLinkToMember(m)));
        ProjectMember currentMember = project.getMembers()
                .stream()
                .filter(member -> member.getAccount().getId().equals(currentAccount.getId()))
                .findAny()
                .orElseThrow(() -> new BadRequestException("User is not a member of the project"));
        if (currentMember.hasPrivilege(PrivilegeCode.MANAGE_TAGS)) {
            resource.add(new Link(MANAGE_HREF, MANAGE_TAGS));
        }
        if (currentMember.hasPrivilege(PrivilegeCode.MANAGE_MEMBERS)) {
            resource.add(new Link(MANAGE_HREF, MANAGE_MEMBERS));
        }
        if (currentMember.hasPrivilege(PrivilegeCode.MANAGE_TASKS)) {
            resource.add(new Link(MANAGE_HREF, MANAGE_TASKS));
        }
        return resource;
    }

    private Link createLinkToMember(ProjectMember member) {
        return links.linkToSingleResource(ProjectMember.class, member.getId()).withRel("currentMember");
    }
}
