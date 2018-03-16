package ru.migmak.planeverything.server.repository;

import org.springframework.data.repository.CrudRepository;
import ru.migmak.planeverything.server.domain.ProjectMember;

public interface ProjectMemberRepository extends CrudRepository<ProjectMember, Long> {
}
