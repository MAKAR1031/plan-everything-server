package ru.migmak.planeverything.server.domain.projection;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.rest.core.config.Projection;
import ru.migmak.planeverything.server.domain.Account;
import ru.migmak.planeverything.server.domain.ProjectMember;

@Projection(name = "full", types = ProjectMember.class)
public interface FullMemberProjection {

    Long getId();

    Account getAccount();

    @Value("#{target.role.name}")
    String getRole();
}
