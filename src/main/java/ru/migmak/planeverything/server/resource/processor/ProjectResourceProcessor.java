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
import ru.migmak.planeverything.server.service.AccountService;

@Component
@Transactional
@RequiredArgsConstructor
public class ProjectResourceProcessor implements ResourceProcessor<Resource<Project>> {

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

        return projectResource;
    }

    private Link createLinkToMember(ProjectMember member) {
        return links.linkToSingleResource(ProjectMember.class, member.getId()).withRel("currentMember");
    }
}
