package ru.migmak.planeverything.server.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RestResource;
import ru.migmak.planeverything.server.domain.Project;

public interface ProjectRepository extends CrudRepository<Project, Long> {
    @Override
    @RestResource(exported = false)
    Iterable<Project> findAll();

    @RestResource(path = "my")
    @Query("select p from Project p where :#{principal.username} in (select m.account.login from p.members m) and p.opened=true")
    Iterable<Project> findMyProjects();

    @Override
    @RestResource(exported = false)
    void deleteAll(Iterable<? extends Project> entities);

    @Override
    @RestResource(exported = false)
    void deleteAll();
}
