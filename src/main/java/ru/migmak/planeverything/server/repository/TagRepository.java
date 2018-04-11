package ru.migmak.planeverything.server.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RestResource;
import ru.migmak.planeverything.server.domain.Tag;

public interface TagRepository extends CrudRepository<Tag, Long> {
    @Override
    @RestResource(exported = false)
    void deleteAll(Iterable<? extends Tag> entities);

    @Override
    @RestResource(exported = false)
    void deleteAll();
}
