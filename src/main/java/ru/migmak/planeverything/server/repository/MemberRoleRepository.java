package ru.migmak.planeverything.server.repository;

import org.springframework.data.repository.CrudRepository;
import ru.migmak.planeverything.server.domain.MemberRole;

import java.util.Optional;

public interface MemberRoleRepository extends CrudRepository<MemberRole, Long> {
    Optional<MemberRole> findByCode(String code);
}
