package ru.migmak.planeverything.server.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import ru.migmak.planeverything.server.domain.Account;

import java.util.Optional;

public interface AccountRepository extends CrudRepository<Account, Long> {
    Optional<Account> findAccountByExternalId(String externalId);

    @Query("select a from Account a where a.externalId = :#{principal}")
    Optional<Account> findCurrentAccount();
}
