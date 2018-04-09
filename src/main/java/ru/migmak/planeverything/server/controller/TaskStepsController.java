package ru.migmak.planeverything.server.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.rest.webmvc.PersistentEntityResource;
import org.springframework.data.rest.webmvc.PersistentEntityResourceAssembler;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.web.bind.annotation.*;
import ru.migmak.planeverything.server.service.TasksService;

@RepositoryRestController
@RequestMapping("/taskSteps/{id}")
@RequiredArgsConstructor
public class TaskStepsController {

    private final TasksService tasksService;

    @PutMapping("/complete")
    @ResponseBody
    public PersistentEntityResource complete(
            @PathVariable("id") Long id,
            @RequestBody(required = false) String report,
            PersistentEntityResourceAssembler assembler
    ) {
        return assembler.toFullResource(tasksService.completeStep(id, report));
    }

}
