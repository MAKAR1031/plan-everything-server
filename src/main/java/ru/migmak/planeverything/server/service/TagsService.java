package ru.migmak.planeverything.server.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.migmak.planeverything.server.domain.Tag;
import ru.migmak.planeverything.server.exception.BadRequestException;
import ru.migmak.planeverything.server.exception.NotFoundException;
import ru.migmak.planeverything.server.repository.AccountRepository;

import static ru.migmak.planeverything.server.domain.enums.PrivilegeCode.MANAGE_TAGS;

@Service
@Transactional
@AllArgsConstructor
public class TagsService {

    private final AccountRepository accountRepository;

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
                .hasPrivilege(MANAGE_TAGS);
    }
}
