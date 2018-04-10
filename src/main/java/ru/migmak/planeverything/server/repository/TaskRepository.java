package ru.migmak.planeverything.server.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RestResource;
import ru.migmak.planeverything.server.domain.Task;

public interface TaskRepository extends CrudRepository<Task, Long> {
    @Override
    @RestResource(exported = false)
    void deleteAll(Iterable<? extends Task> entities);

    @Override
    @RestResource(exported = false)
    void deleteAll();
}
