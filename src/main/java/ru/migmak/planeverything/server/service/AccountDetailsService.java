package ru.migmak.planeverything.server.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.migmak.planeverything.server.domain.Account;
import ru.migmak.planeverything.server.repository.AccountRepository;

@Service
@RequiredArgsConstructor
public class AccountDetailsService implements UserDetailsService {

    private final AccountRepository repository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Account account = repository.findAccountByLogin(username)
                .orElseThrow(() -> new UsernameNotFoundException("Could not found account with login " + username));
        return new User(
                account.getLogin(),
                account.getPassword(),
                true,
                true,
                true,
                !account.isBlocked(),
                AuthorityUtils.createAuthorityList(account.getRole().getCode())
        );
    }
}
