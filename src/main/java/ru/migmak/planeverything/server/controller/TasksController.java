package ru.migmak.planeverything.server.controller;

import org.springframework.data.rest.webmvc.PersistentEntityResource;
import org.springframework.data.rest.webmvc.PersistentEntityResourceAssembler;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.web.bind.annotation.*;
import ru.migmak.planeverything.server.service.TasksService;

@RepositoryRestController
@RequestMapping("/tasks/{id}")
public class TasksController {

    private final TasksService tasksService;

    public TasksController(TasksService tasksService) {
        this.tasksService = tasksService;
    }

    @RequestMapping(path = "/assign", method = RequestMethod.PUT)
    @ResponseBody
    public PersistentEntityResource assign(
            @PathVariable("id") Long taskId,
            @RequestBody Long memberId,
            PersistentEntityResourceAssembler assembler
    ) {
        return assembler.toFullResource(tasksService.assign(taskId, memberId));
    }
}
