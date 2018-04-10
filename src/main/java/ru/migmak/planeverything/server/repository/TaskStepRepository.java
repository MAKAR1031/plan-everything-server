package ru.migmak.planeverything.server.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RestResource;
import ru.migmak.planeverything.server.domain.TaskStep;

public interface TaskStepRepository extends CrudRepository<TaskStep, Long> {
    @Override
    @RestResource(exported = false)
    void deleteAll(Iterable<? extends TaskStep> entities);

    @Override
    @RestResource(exported = false)
    void deleteAll();
}
