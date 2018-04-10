package ru.migmak.planeverything.server.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RestResource;
import ru.migmak.planeverything.server.domain.Criterion;

public interface CriterionRepository extends CrudRepository<Criterion, Long> {

    @Override
    @RestResource(exported = false)
    void deleteAll(Iterable<? extends Criterion> entities);

    @Override
    @RestResource(exported = false)
    void deleteAll();
}
