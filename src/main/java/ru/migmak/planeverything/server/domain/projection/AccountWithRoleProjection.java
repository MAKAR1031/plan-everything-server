package ru.migmak.planeverything.server.domain.projection;

import org.springframework.data.rest.core.config.Projection;
import ru.migmak.planeverything.server.domain.Account;
import ru.migmak.planeverything.server.domain.AccountRole;

@Projection(name = "withRole", types = Account.class)
@SuppressWarnings("unused")
public interface AccountWithRoleProjection {
    String getLogin();

    String getEmail();

    String getFullName();

    AccountRole getRole();
}
