package ru.migmak.planeverything.server.domain.projection;

import org.springframework.data.rest.core.config.Projection;
import ru.migmak.planeverything.server.domain.MemberRole;

@Projection(name = "minimal", types = {MemberRole.class})
public interface MinimalMemberRoleProjection {
    String getName();
    String getCode();
}
