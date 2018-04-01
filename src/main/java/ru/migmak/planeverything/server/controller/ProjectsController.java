package ru.migmak.planeverything.server.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.PersistentEntityResource;
import org.springframework.data.rest.webmvc.PersistentEntityResourceAssembler;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.web.bind.annotation.*;
import ru.migmak.planeverything.server.ro.ProjectRo;
import ru.migmak.planeverything.server.service.ProjectsService;

@RepositoryRestController
@RequestMapping("/projects")
public class ProjectsController {

    private ProjectsService projectsService;

    @Autowired
    public ProjectsController(ProjectsService projectsService) {
        this.projectsService = projectsService;
    }

    @RequestMapping(path = "/create", method = RequestMethod.POST)
    @ResponseBody
    public PersistentEntityResource create(@RequestBody ProjectRo project,
                                           PersistentEntityResourceAssembler assembler) {
        return assembler.toFullResource(projectsService.create(project));
    }

    @RequestMapping(path = "/{id}/close", method = RequestMethod.PATCH)
    @ResponseBody
    public PersistentEntityResource close(@PathVariable("id") Long id, PersistentEntityResourceAssembler assembler) {
        return assembler.toFullResource(projectsService.close(id));
    }
}
