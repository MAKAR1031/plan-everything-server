package ru.migmak.planeverything.server.resource;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.hateoas.ResourceSupport;

@Getter
@AllArgsConstructor
public class ProjectProgress extends ResourceSupport {
    private Long completedSteps;
    private Long totalSteps;
}
