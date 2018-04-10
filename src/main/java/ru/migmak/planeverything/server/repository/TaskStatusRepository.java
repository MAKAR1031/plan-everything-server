package ru.migmak.planeverything.server.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RestResource;
import ru.migmak.planeverything.server.domain.TaskStatus;

import java.util.Optional;

public interface TaskStatusRepository extends CrudRepository<TaskStatus, Long> {
    Optional<TaskStatus> findByCode(String code);

    @Override
    @RestResource(exported = false)
    <S extends TaskStatus> S save(S entity);

    @Override
    @RestResource(exported = false)
    <S extends TaskStatus> Iterable<S> saveAll(Iterable<S> entities);

    @Override
    @RestResource(exported = false)
    void deleteById(Long id);

    @Override
    @RestResource(exported = false)
    void delete(TaskStatus entity);

    @Override
    @RestResource(exported = false)
    void deleteAll(Iterable<? extends TaskStatus> entities);

    @Override
    @RestResource(exported = false)
    void deleteAll();
}
