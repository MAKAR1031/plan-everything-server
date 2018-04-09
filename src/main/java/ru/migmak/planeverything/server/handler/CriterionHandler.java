package ru.migmak.planeverything.server.handler;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.rest.core.annotation.HandleBeforeCreate;
import org.springframework.data.rest.core.annotation.HandleBeforeSave;
import org.springframework.data.rest.core.annotation.RepositoryEventHandler;
import org.springframework.stereotype.Component;
import ru.migmak.planeverything.server.domain.Criterion;
import ru.migmak.planeverything.server.exception.BadRequestException;

@Component
@RepositoryEventHandler
@Slf4j
@RequiredArgsConstructor
public class CriterionHandler {

    @HandleBeforeCreate
    public void handleCreate(Criterion criterion) {
        if (criterion.getTask().getStatus().isEditable()) {
            throw new BadRequestException("Task can edited only 'CREATED' state");
        }
        if (criterion.getActualValue() != null) {
            throw new BadRequestException("The actual value should not be specified when creating");
        }
    }

    @HandleBeforeSave
    public void handeUpdate(Criterion criterion) {
        if (!criterion.getTask().getStatus().isCompleted()) {
            if (criterion.getActualValue() != null) {
                throw new BadRequestException("The actual value should not be specified in this state");
            }
        } else {
            if (criterion.getActualValue() == null) {
                throw new BadRequestException("The actual value should be specified");
            }
        }
    }
}
