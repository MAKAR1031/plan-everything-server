package ru.migmak.planeverything.server.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.migmak.planeverything.server.domain.Account;
import ru.migmak.planeverything.server.domain.AccountRole;
import ru.migmak.planeverything.server.exception.BadRequestException;
import ru.migmak.planeverything.server.exception.NotFoundException;
import ru.migmak.planeverything.server.exception.ServiceException;
import ru.migmak.planeverything.server.repository.AccountRepository;
import ru.migmak.planeverything.server.repository.AccountRoleRepository;

import static ru.migmak.planeverything.server.domain.enums.AccountRoleCode.USER;

@Service
@Transactional
@RequiredArgsConstructor
public class AccountService {
    private final AccountRepository accountRepository;
    private final AccountRoleRepository roleRepository;
    private final PasswordEncoder encoder;

    public void register(Account account) {
        AccountRole role = roleRepository.findByCode(USER.name())
                .orElseThrow(() -> new ServiceException("Account role 'USER' not found"));
        account.setRole(role);
        account.setBlocked(false);
        account.setPassword(encoder.encode(account.getPassword()));
        accountRepository.save(account);
    }

    public Account setBlocked(Long id, boolean blocked) {
        Account account = accountRepository.findById(id).orElseThrow(NotFoundException::new);
        if (account.isBlocked() == blocked) {
            throw new BadRequestException(blocked ? "Account already blocked" : "Account already unlocked");
        }
        account.setBlocked(blocked);
        return accountRepository.save(account);
    }

    public Account getCurrentAccount() {
        return accountRepository.findCurrentAccount()
                .orElseThrow(() -> new ServiceException("Current account not found"));
    }

    public Account findByLogin(String login) {
        return accountRepository.findAccountByLogin(login).orElse(null);
    }
}
