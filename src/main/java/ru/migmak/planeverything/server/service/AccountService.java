package ru.migmak.planeverything.server.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.migmak.planeverything.server.domain.Account;
import ru.migmak.planeverything.server.exception.BadRequestException;
import ru.migmak.planeverything.server.exception.NotFoundException;
import ru.migmak.planeverything.server.repository.AccountRepository;

@Service
@Transactional
@RequiredArgsConstructor
public class AccountService {
    private final AccountRepository repository;

    public Account setBlocked(Long id, boolean blocked) {
        Account account = repository.findById(id).orElseThrow(NotFoundException::new);
        if (account.isBlocked() == blocked) {
            throw new BadRequestException(blocked ? "Account already blocked" : "Account already unlocked");
        }
        account.setBlocked(blocked);
        return repository.save(account);
    }
}
