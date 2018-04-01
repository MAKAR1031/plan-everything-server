package ru.migmak.planeverything.server.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.security.access.prepost.PreAuthorize;
import ru.migmak.planeverything.server.domain.Tag;

public interface TagRepository extends CrudRepository<Tag, Long> {

    @Override
    @PreAuthorize("@tagsService.checkEditAccess(#tag)")
    <T extends Tag> T save(@Param("tag") T tag);

    @Override
    @PreAuthorize("@tagsService.checkEditAccess(#tag)")
    void delete(@Param("tag") Tag tag);
}
