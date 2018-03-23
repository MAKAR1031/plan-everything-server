package ru.migmak.planeverything.server.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.oauth2.resource.AuthoritiesExtractor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.stereotype.Component;
import ru.migmak.planeverything.server.domain.Account;
import ru.migmak.planeverything.server.repository.AccountRepository;
import ru.migmak.planeverything.server.repository.AccountRoleRepository;

import java.util.List;
import java.util.Map;

import static ru.migmak.planeverything.server.domain.enums.AccountRoleCode.ADMIN;
import static ru.migmak.planeverything.server.domain.enums.AccountRoleCode.USER;

@Component
public class AccountRoleExtractor implements AuthoritiesExtractor {

    private AccountRepository accountRepository;
    private AccountRoleRepository roleRepository;

    @Autowired
    public AccountRoleExtractor(AccountRepository accountRepository, AccountRoleRepository roleRepository) {
        this.accountRepository = accountRepository;
        this.roleRepository = roleRepository;
    }

    @Override
    public List<GrantedAuthority> extractAuthorities(Map<String, Object> map) {
        addAdminAccountIfNotExists(map);
        String id = (String) map.get("id");
        Account account = accountRepository.findAccountByExternalId(id).orElseGet(() -> {
            Account newAccount = buildFromProperties(map);
            newAccount.setRole(roleRepository.findByCode(USER.name()).orElseThrow(IllegalStateException::new));
            accountRepository.save(newAccount);
            return newAccount;
        });
        if (account.isBlocked()) {
            throw new BadCredentialsException("Account blocked");
        }
        return AuthorityUtils.commaSeparatedStringToAuthorityList(account.getRole().getCode());
    }

    private void addAdminAccountIfNotExists(Map<String, Object> map) {
        if (accountRepository.count() != 0) {
            return;
        }
        roleRepository.findByCode(ADMIN.name()).ifPresent(adminRole -> {
            Account adminAccount = buildFromProperties(map);
            adminAccount.setRole(adminRole);
            accountRepository.save(adminAccount);
        });
    }

    private Account buildFromProperties(Map<String, Object> properties) {
        Account account = new Account();
        account.setFullName((String) properties.get("name"));
        account.setExternalId((String) properties.get("id"));
        account.setBlocked(false);
        return account;
    }
}
