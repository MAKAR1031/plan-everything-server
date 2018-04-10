package ru.migmak.planeverything.server.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RestResource;
import ru.migmak.planeverything.server.domain.Account;

import java.util.Optional;

public interface AccountRepository extends CrudRepository<Account, Long> {
    @RestResource(exported = false)
    Optional<Account> findAccountByLogin(String login);

    @Query("select a from Account a where a.login = :#{principal.username}")
    Optional<Account> findCurrentAccount();

    @Override
    @RestResource(exported = false)
    void deleteAll(Iterable<? extends Account> entities);

    @Override
    @RestResource(exported = false)
    void deleteAll();
}
