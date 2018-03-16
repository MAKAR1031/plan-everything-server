package ru.migmak.planeverything.server.repository;

import org.springframework.data.repository.CrudRepository;
import ru.migmak.planeverything.server.domain.TaskStep;

public interface TaskStepRepository extends CrudRepository<TaskStep, Long> {
}
