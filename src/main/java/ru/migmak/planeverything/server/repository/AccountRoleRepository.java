package ru.migmak.planeverything.server.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RestResource;
import ru.migmak.planeverything.server.domain.AccountRole;

import java.util.Optional;

public interface AccountRoleRepository extends CrudRepository<AccountRole, Long> {
    Optional<AccountRole> findByCode(String code);

    @Override
    @RestResource(exported = false)
    void deleteById(Long id);

    @Override
    @RestResource(exported = false)
    void delete(AccountRole entity);

    @Override
    @RestResource(exported = false)
    void deleteAll(Iterable<? extends AccountRole> entities);

    @Override
    @RestResource(exported = false)
    void deleteAll();
}
