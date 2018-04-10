package ru.migmak.planeverything.server.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RestResource;
import ru.migmak.planeverything.server.domain.Privilege;

public interface PrivilegeRepository extends CrudRepository<Privilege, Long> {

    @Override
    @RestResource(exported = false)
    <S extends Privilege> S save(S entity);

    @Override
    @RestResource(exported = false)
    <S extends Privilege> Iterable<S> saveAll(Iterable<S> entities);

    @Override
    @RestResource(exported = false)
    void deleteById(Long id);

    @Override
    @RestResource(exported = false)
    void delete(Privilege entity);

    @Override
    @RestResource(exported = false)
    void deleteAll(Iterable<? extends Privilege> entities);

    @Override
    @RestResource(exported = false)
    void deleteAll();
}
