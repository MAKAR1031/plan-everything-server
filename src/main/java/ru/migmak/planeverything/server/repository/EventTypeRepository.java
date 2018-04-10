package ru.migmak.planeverything.server.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RestResource;
import ru.migmak.planeverything.server.domain.EventType;

import java.util.Optional;

public interface EventTypeRepository extends CrudRepository<EventType, Long> {
    Optional<EventType> findByCode(String code);

    @Override
    @RestResource(exported = false)
    void deleteById(Long id);

    @Override
    @RestResource(exported = false)
    void delete(EventType entity);

    @Override
    @RestResource(exported = false)
    void deleteAll(Iterable<? extends EventType> entities);

    @Override
    @RestResource(exported = false)
    void deleteAll();
}
