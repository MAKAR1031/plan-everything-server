package ru.migmak.planeverything.server.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.migmak.planeverything.server.domain.Account;
import ru.migmak.planeverything.server.exception.InvalidAccountBlockStateException;
import ru.migmak.planeverything.server.exception.NotFoundException;
import ru.migmak.planeverything.server.repository.AccountRepository;

@Service
@Transactional
public class AccountService {
    private final AccountRepository repository;

    @Autowired
    public AccountService(AccountRepository repository) {
        this.repository = repository;
    }

    public Account setBlocked(Long id, boolean blocked) {
        Account account = repository.findById(id).orElseThrow(NotFoundException::new);
        if (account.isBlocked() == blocked) {
            throw new InvalidAccountBlockStateException();
        }
        account.setBlocked(blocked);
        return repository.save(account);
    }
}
