package ru.migmak.planeverything.server.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.rest.webmvc.PersistentEntityResource;
import org.springframework.data.rest.webmvc.PersistentEntityResourceAssembler;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import ru.migmak.planeverything.server.service.AccountService;

@RepositoryRestController
@RequestMapping("/accounts/{id}")
@RequiredArgsConstructor
public class AccountsController {

    private final AccountService accountService;

    @PostMapping("/block")
    @ResponseBody
    public PersistentEntityResource block(@PathVariable("id") Long id, PersistentEntityResourceAssembler assembler) {
        return assembler.toFullResource(accountService.setBlocked(id, true));
    }

    @PostMapping("/unblock")
    @ResponseBody
    public PersistentEntityResource unblock(@PathVariable("id") Long id, PersistentEntityResourceAssembler assembler) {
        return assembler.toFullResource(accountService.setBlocked(id, false));
    }
}
