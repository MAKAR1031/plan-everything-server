package ru.migmak.planeverything.server.repository;

import org.springframework.data.repository.CrudRepository;
import ru.migmak.planeverything.server.domain.AccountRole;

import java.util.Optional;

public interface AccountRoleRepository extends CrudRepository<AccountRole, Long> {
    Optional<AccountRole> findByCode(String code);
}
