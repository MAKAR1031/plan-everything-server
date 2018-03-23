package ru.migmak.planeverything.server.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RestResource;
import ru.migmak.planeverything.server.domain.Project;

import java.util.List;

public interface ProjectRepository extends CrudRepository<Project, Long> {
    @Override
    @RestResource(exported = false)
    Iterable<Project> findAll();

    @Query("select p from Project p where :#{principal} in (select m.account.externalId from p.members m)")
    @RestResource(path = "my")
    List<Project> findProjectsByAccountExternalId();
}
