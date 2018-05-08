package ru.migmak.planeverything.server.domain.projection;

import org.springframework.data.rest.core.config.Projection;
import ru.migmak.planeverything.server.domain.Account;
import ru.migmak.planeverything.server.domain.MemberRole;
import ru.migmak.planeverything.server.domain.ProjectMember;

@Projection(name = "full", types = ProjectMember.class)
@SuppressWarnings("unused")
public interface FullMemberProjection {

    Long getId();

    Account getAccount();

    MemberRole getRole();
}
