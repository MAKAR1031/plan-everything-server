package ru.migmak.planeverything.server.repository;

import org.springframework.data.repository.CrudRepository;
import ru.migmak.planeverything.server.domain.EventType;

import java.util.Optional;

public interface EventTypeRepository extends CrudRepository<EventType, Long> {
    Optional<EventType> findByCode(String code);
}
