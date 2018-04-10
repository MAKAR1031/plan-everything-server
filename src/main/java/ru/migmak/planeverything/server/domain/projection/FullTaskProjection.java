package ru.migmak.planeverything.server.domain.projection;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.rest.core.config.Projection;
import ru.migmak.planeverything.server.domain.Criterion;
import ru.migmak.planeverything.server.domain.Tag;
import ru.migmak.planeverything.server.domain.Task;
import ru.migmak.planeverything.server.domain.TaskStep;

import java.util.List;

@Projection(name = "full", types = Task.class)
@SuppressWarnings("unused")
public interface FullTaskProjection {

    FullProjectMemberProjection getAuthor();

    FullProjectMemberProjection getAssignee();

    @Value("#{target.status.name}")
    String getStatus();

    List<Tag> getTags();

    List<TaskStep> getSteps();

    List<Criterion> getCriteria();
}
