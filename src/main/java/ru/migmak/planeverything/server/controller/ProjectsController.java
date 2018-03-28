package ru.migmak.planeverything.server.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.PersistentEntityResource;
import org.springframework.data.rest.webmvc.PersistentEntityResourceAssembler;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
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

}
