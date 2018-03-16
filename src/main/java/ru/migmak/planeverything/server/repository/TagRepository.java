package ru.migmak.planeverything.server.repository;

import org.springframework.data.repository.CrudRepository;
import ru.migmak.planeverything.server.domain.Tag;

public interface TagRepository extends CrudRepository<Tag, Long> {
}
