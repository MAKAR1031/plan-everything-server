package ru.migmak.planeverything.server.repository;

import org.springframework.data.repository.CrudRepository;
import ru.migmak.planeverything.server.domain.TaskStatus;

import java.util.Optional;

public interface TaskStatusRepository extends CrudRepository<TaskStatus, Long> {
    Optional<TaskStatus> findByCode(String code);
}
