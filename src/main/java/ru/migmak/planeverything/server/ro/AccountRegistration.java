package ru.migmak.planeverything.server.ro;

import lombok.Getter;
import lombok.Setter;
import ru.migmak.planeverything.server.domain.Account;

@Getter
@Setter
public class AccountRegistration {
    private String login;
    private String password;
    private String email;
    private String fullName;

    public Account toAccount() {
        Account account = new Account();
        account.setLogin(login);
        account.setPassword(password);
        account.setEmail(email);
        account.setFullName(fullName);
        return account;
    }
}
