package ru.migmak.planeverything.server.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.migmak.planeverything.server.domain.Tag;
import ru.migmak.planeverything.server.domain.enums.PrivilegeCode;
import ru.migmak.planeverything.server.exception.BadRequestException;
import ru.migmak.planeverything.server.exception.NotFoundException;
import ru.migmak.planeverything.server.repository.AccountRepository;

@Service
@Transactional
public class TagsService {

    private final AccountRepository accountRepository;

    @Autowired
    public TagsService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    public boolean checkEditAccess(Tag tag) {
        if (tag.getProject() == null) {
            throw new BadRequestException();
        }
        return tag
                .getProject()
                .findMemberByAccountId(accountRepository
                        .findCurrentAccount()
                        .orElseThrow(NotFoundException::new)
                        .getId())
                .hasPrivilege(PrivilegeCode.MANAGE_TAGS.name());
    }
}
