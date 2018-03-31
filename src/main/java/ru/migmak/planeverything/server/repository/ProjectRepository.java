package ru.migmak.planeverything.server.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RestResource;
import ru.migmak.planeverything.server.domain.Project;

public interface ProjectRepository extends CrudRepository<Project, Long> {
    @Override
    @RestResource(exported = false)
    Iterable<Project> findAll();
}
