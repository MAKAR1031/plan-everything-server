package ru.migmak.planeverything.server.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.rest.webmvc.PersistentEntityResource;
import org.springframework.data.rest.webmvc.PersistentEntityResourceAssembler;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.web.bind.annotation.*;
import ru.migmak.planeverything.server.service.AccountService;

@RepositoryRestController
@RequestMapping("/accounts")
@RequiredArgsConstructor
public class AccountsController {

    private final AccountService accountService;

    @GetMapping("/me")
    @ResponseBody
    public PersistentEntityResource me(PersistentEntityResourceAssembler assembler) {
        return assembler.toFullResource(accountService.getCurrentAccount());
    }

    @PostMapping("/{id}/lock")
    @ResponseBody
    public PersistentEntityResource lock(@PathVariable("id") Long id, PersistentEntityResourceAssembler assembler) {
        return assembler.toFullResource(accountService.setBlocked(id, true));
    }

    @PostMapping("/{id}/unlock")
    @ResponseBody
    public PersistentEntityResource unlock(@PathVariable("id") Long id, PersistentEntityResourceAssembler assembler) {
        return assembler.toFullResource(accountService.setBlocked(id, false));
    }
}
