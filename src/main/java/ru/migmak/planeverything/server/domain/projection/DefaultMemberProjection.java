package ru.migmak.planeverything.server.domain.projection;

import org.springframework.data.rest.core.config.Projection;
import ru.migmak.planeverything.server.domain.ProjectMember;

@Projection(name = "default", types = {ProjectMember.class})
public interface DefaultMemberProjection {
    MinimalAccountProjection getAccount();
    MinimalMemberRoleProjection getRole();
}
