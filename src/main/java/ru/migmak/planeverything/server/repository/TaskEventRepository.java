package ru.migmak.planeverything.server.repository;

import org.springframework.data.repository.CrudRepository;
import ru.migmak.planeverything.server.domain.TaskEvent;

public interface TaskEventRepository extends CrudRepository<TaskEvent, Long> {
}
