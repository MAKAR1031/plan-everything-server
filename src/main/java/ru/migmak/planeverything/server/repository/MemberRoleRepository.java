package ru.migmak.planeverything.server.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import ru.migmak.planeverything.server.domain.MemberRole;

import java.util.Optional;

@RepositoryRestResource(exported = false)
public interface MemberRoleRepository extends CrudRepository<MemberRole, Long> {
    Optional<MemberRole> findByName(String name);
}
