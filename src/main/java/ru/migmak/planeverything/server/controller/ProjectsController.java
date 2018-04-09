package ru.migmak.planeverything.server.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.rest.webmvc.PersistentEntityResource;
import org.springframework.data.rest.webmvc.PersistentEntityResourceAssembler;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import ru.migmak.planeverything.server.service.ProjectsService;

@RepositoryRestController
@RequestMapping("/projects")
@RequiredArgsConstructor
public class ProjectsController {

    private final ProjectsService projectsService;

    @PatchMapping("/{id}/close")
    @ResponseBody
    public PersistentEntityResource close(@PathVariable("id") Long id, PersistentEntityResourceAssembler assembler) {
        return assembler.toFullResource(projectsService.close(id));
    }
}
