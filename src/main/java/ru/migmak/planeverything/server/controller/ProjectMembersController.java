package ru.migmak.planeverything.server.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.migmak.planeverything.server.ro.NewMemberRo;
import ru.migmak.planeverything.server.service.ProjectsService;

@RepositoryRestController
@RequestMapping("/projects/{id}/members")
public class ProjectMembersController {

    private final ProjectsService projectsService;

    @Autowired
    public ProjectMembersController(ProjectsService projectsService) {
        this.projectsService = projectsService;
    }

    @RequestMapping(path = "/add", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public void addMember(@PathVariable("id") Long id, @RequestBody NewMemberRo newMemberRo) {
        projectsService.addMember(id, newMemberRo.getAccountId(), newMemberRo.getRoleId());
    }

    @RequestMapping(path = "/{memberId}/exclude", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public void excludeMember(@PathVariable("id") Long id, @PathVariable("memberId") Long memberId) {
        projectsService.excludeMember(id, memberId);
    }

    @RequestMapping(path = "/{memberId}/change_role/{role_id}", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public void changeRole(
            @PathVariable("id") Long id,
            @PathVariable("memberId") Long memberId,
            @PathVariable("role_id") Long roleId
    ) {
        projectsService.changeMemberRole(id, memberId, roleId);
    }
}
