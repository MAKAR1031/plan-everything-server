package ru.migmak.planeverything.server.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;
import ru.migmak.planeverything.server.domain.ProjectMember;
import ru.migmak.planeverything.server.domain.projection.DefaultMemberProjection;

@RepositoryRestResource(excerptProjection = DefaultMemberProjection.class, path = "members")
public interface ProjectMemberRepository extends CrudRepository<ProjectMember, Long> {

    @Override
    @RestResource(exported = false)
    void deleteAll(Iterable<? extends ProjectMember> entities);

    @Override
    @RestResource(exported = false)
    void deleteAll();
}
