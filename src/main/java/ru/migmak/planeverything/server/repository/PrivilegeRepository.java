package ru.migmak.planeverything.server.repository;

import org.springframework.data.repository.CrudRepository;
import ru.migmak.planeverything.server.domain.Privilege;

public interface PrivilegeRepository extends CrudRepository<Privilege, Long> {
}
