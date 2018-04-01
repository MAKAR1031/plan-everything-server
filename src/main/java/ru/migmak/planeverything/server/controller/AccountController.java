package ru.migmak.planeverything.server.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.PersistentEntityResource;
import org.springframework.data.rest.webmvc.PersistentEntityResourceAssembler;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import ru.migmak.planeverything.server.service.AccountService;

@RepositoryRestController
@RequestMapping("/accounts/{id}")
public class AccountController {

    private final AccountService accountService;

    @Autowired
    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @RequestMapping(path = "/block", method = RequestMethod.POST)
    @ResponseBody
    public PersistentEntityResource block(@PathVariable("id") Long id, PersistentEntityResourceAssembler assembler) {
        return assembler.toFullResource(accountService.setBlocked(id, true));
    }

    @RequestMapping(path = "/unblock", method = RequestMethod.POST)
    @ResponseBody
    public PersistentEntityResource unblock(@PathVariable("id") Long id, PersistentEntityResourceAssembler assembler) {
        return assembler.toFullResource(accountService.setBlocked(id, false));
    }
}
