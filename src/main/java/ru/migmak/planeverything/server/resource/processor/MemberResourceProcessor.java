package ru.migmak.planeverything.server.resource.processor;

import lombok.RequiredArgsConstructor;
import org.springframework.data.rest.webmvc.support.RepositoryEntityLinks;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.ResourceProcessor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.migmak.planeverything.server.domain.Account;
import ru.migmak.planeverything.server.domain.ProjectMember;
import ru.migmak.planeverything.server.domain.enums.PrivilegeCode;
import ru.migmak.planeverything.server.domain.projection.FullMemberProjection;
import ru.migmak.planeverything.server.exception.BadRequestException;
import ru.migmak.planeverything.server.repository.ProjectMemberRepository;
import ru.migmak.planeverything.server.service.AccountService;

@Component
@Transactional
@RequiredArgsConstructor
public class MemberResourceProcessor implements ResourceProcessor<Resource<FullMemberProjection>> {

    private final AccountService accountService;
    private final ProjectMemberRepository memberRepository;
    private final RepositoryEntityLinks links;

    @Override
    public Resource<FullMemberProjection> process(Resource<FullMemberProjection> resource) {
        Account currentAccount = accountService.getCurrentAccount();
        Long id = resource.getContent().getId();
        ProjectMember member = memberRepository.findById(id)
                .orElseThrow(() -> new BadRequestException("Could not find project member with id: " + id));
        ProjectMember currentMember = member.getProject()
                .getMembers()
                .stream()
                .filter(m -> m.getAccount().getId().equals(currentAccount.getId()))
                .findAny()
                .orElseThrow(() -> new BadRequestException("User is not a member of the project"));
        if (!currentMember.getId().equals(member.getId()) && currentMember.hasPrivilege(PrivilegeCode.MANAGE_MEMBERS)) {
            resource.add(links.linkToSingleResource(ProjectMember.class, member.getId()).withRel("exclude"));
            resource.add(links.linkToSingleResource(ProjectMember.class, member.getId()).withRel("changeRole"));
        }
        return resource;
    }
}
