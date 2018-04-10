package ru.migmak.planeverything.server.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RestResource;
import ru.migmak.planeverything.server.domain.MemberRole;

import java.util.Optional;

public interface MemberRoleRepository extends CrudRepository<MemberRole, Long> {
    Optional<MemberRole> findByCode(String code);

    @Override
    @RestResource(exported = false)
    void deleteById(Long id);

    @Override
    @RestResource(exported = false)
    void delete(MemberRole entity);

    @Override
    @RestResource(exported = false)
    void deleteAll(Iterable<? extends MemberRole> entities);

    @Override
    @RestResource(exported = false)
    void deleteAll();
}
