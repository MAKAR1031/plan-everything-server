package ru.migmak.planeverything.server.domain.projection;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.rest.core.config.Projection;
import ru.migmak.planeverything.server.domain.TaskEvent;

import java.util.Date;

@Projection(name = "withFullName", types = TaskEvent.class)
@SuppressWarnings("unused")
public interface EventWithFullNameProjection {
    String getName();

    Date getTime();

    @Value("#{target.initiator.fullName}")
    String getFullName();
}
