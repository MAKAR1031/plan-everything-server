package ru.migmak.planeverything.server.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RestResource;
import ru.migmak.planeverything.server.domain.Account;
import ru.migmak.planeverything.server.domain.Project;

import java.util.List;
import java.util.Optional;

public interface AccountRepository extends CrudRepository<Account, Long> {
    @RestResource(exported = false)
    Optional<Account> findAccountByLogin(String login);

    @RestResource(path = "me", rel = "me")
    @Query("select a from Account a where a.login = :#{principal.username}")
    Optional<Account> findCurrentAccount();

    @Override
    @RestResource(exported = false)
    void deleteAll(Iterable<? extends Account> entities);

    @Override
    @RestResource(exported = false)
    void deleteAll();

    @RestResource(path = "nonInProject", rel = "nonInProject")
    @Query("select a from Account a where a.id in :#{@projectsService.accountMemberIds(#project)} and a.role.code <> 'ADMIN'")
    List<Account> findNonProject(@Param("project") Project project);
}
