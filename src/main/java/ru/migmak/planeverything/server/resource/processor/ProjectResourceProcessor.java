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

    private static final String MANAGE_TAGS = "manageTags";
    private final AccountService accountService;
    private final RepositoryEntityLinks links;

    @Override
    public Resource<Project> process(Resource<Project> projectResource) {
        final Project project = projectResource.getContent();
        Account currentAccount = accountService.getCurrentAccount();
        project.getMembers().stream()
                .filter(m -> m.getAccount().getId().equals(currentAccount.getId()))
                .findFirst()
                .ifPresent(m -> projectResource.add(createLinkToMember(m)));
        ProjectMember currentMember = project.getMembers()
                .stream()
                .filter(member -> member.getAccount().getId().equals(currentAccount.getId()))
                .findAny()
                .orElseThrow(() -> new BadRequestException("User is not a member of the project"));
        if (currentMember.hasPrivilege(PrivilegeCode.MANAGE_TAGS)) {
            projectResource.add(new Link(MANAGE_TAGS, MANAGE_TAGS));
        }
        return projectResource;
    }

    private Link createLinkToMember(ProjectMember member) {
        return links.linkToSingleResource(ProjectMember.class, member.getId()).withRel("currentMember");
    }
}
