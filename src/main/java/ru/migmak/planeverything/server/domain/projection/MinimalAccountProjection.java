package ru.migmak.planeverything.server.domain.projection;

import org.springframework.data.rest.core.config.Projection;
import ru.migmak.planeverything.server.domain.Account;

@Projection(name = "minimal", types = {Account.class})
public interface MinimalAccountProjection {
    String getLogin();
    String getFullName();
    String getEmail();
}
