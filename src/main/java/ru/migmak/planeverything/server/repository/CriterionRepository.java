package ru.migmak.planeverything.server.repository;

import org.springframework.data.repository.CrudRepository;
import ru.migmak.planeverything.server.domain.Criterion;

public interface CriterionRepository extends CrudRepository<Criterion, Long> {
}
