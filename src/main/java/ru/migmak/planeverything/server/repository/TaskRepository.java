package ru.migmak.planeverything.server.repository;

import org.springframework.data.repository.CrudRepository;
import ru.migmak.planeverything.server.domain.Task;

public interface TaskRepository extends CrudRepository<Task, Long> {
}
