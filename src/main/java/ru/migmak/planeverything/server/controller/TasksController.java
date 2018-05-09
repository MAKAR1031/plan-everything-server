package ru.migmak.planeverything.server.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.rest.webmvc.PersistentEntityResource;
import org.springframework.data.rest.webmvc.PersistentEntityResourceAssembler;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.hateoas.ResourceSupport;
import org.springframework.web.bind.annotation.*;
import ru.migmak.planeverything.server.service.TasksService;

@RepositoryRestController
@RequestMapping("/tasks/{id}")
@RequiredArgsConstructor
@SuppressWarnings("ConstantConditions")
public class TasksController {

    private final TasksService tasksService;

    @PutMapping("/assign")
    @ResponseBody
    public PersistentEntityResource assign(
            @PathVariable("id") Long id,
            @RequestBody Long memberId,
            PersistentEntityResourceAssembler assembler
    ) {
        return assembler.toFullResource(tasksService.assign(id, memberId));
    }

    @PutMapping("/start")
    @ResponseBody
    public PersistentEntityResource start(@PathVariable("id") Long id, PersistentEntityResourceAssembler assembler) {
        return assembler.toFullResource(tasksService.startTask(id));
    }

    @PutMapping("/estimate")
    @ResponseBody
    public PersistentEntityResource estimate(@PathVariable("id") Long id, PersistentEntityResourceAssembler assembler) {
        return assembler.toFullResource(tasksService.estimate(id));
    }

    @GetMapping("/updateInfo")
    @ResponseBody
    public ResourceSupport updateInfo(@PathVariable("id") Long id) {
        return tasksService.getUpdateInfo(id);
    }
}
