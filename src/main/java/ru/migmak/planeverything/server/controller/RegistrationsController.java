package ru.migmak.planeverything.server.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.migmak.planeverything.server.ro.AccountRegistration;
import ru.migmak.planeverything.server.service.AccountService;

@RestController
@RequestMapping("/register")
@RequiredArgsConstructor
public class RegistrationsController {

    private final AccountService accountService;

    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    public void register(@RequestBody AccountRegistration account) {
        accountService.register(account.toAccount());
    }
}
