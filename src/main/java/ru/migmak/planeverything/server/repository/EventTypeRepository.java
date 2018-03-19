package ru.migmak.planeverything.server.repository;

import org.springframework.data.repository.CrudRepository;
import ru.migmak.planeverything.server.domain.EventType;

public interface EventTypeRepository extends CrudRepository<EventType, Long> {
}
