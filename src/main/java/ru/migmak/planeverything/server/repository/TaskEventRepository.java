package ru.migmak.planeverything.server.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RestResource;
import ru.migmak.planeverything.server.domain.TaskEvent;

public interface TaskEventRepository extends CrudRepository<TaskEvent, Long> {
    @Override
    @RestResource(exported = false)
    <S extends TaskEvent> S save(S entity);

    @Override
    @RestResource(exported = false)
    <S extends TaskEvent> Iterable<S> saveAll(Iterable<S> entities);

    @Override
    @RestResource(exported = false)
    void deleteById(Long id);

    @Override
    @RestResource(exported = false)
    void delete(TaskEvent entity);

    @Override
    @RestResource(exported = false)
    void deleteAll(Iterable<? extends TaskEvent> entities);

    @Override
    @RestResource(exported = false)
    void deleteAll();
}
