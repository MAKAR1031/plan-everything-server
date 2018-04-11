package ru.migmak.planeverything.server.handler;

import lombok.RequiredArgsConstructor;
import org.springframework.data.rest.core.annotation.HandleBeforeCreate;
import org.springframework.data.rest.core.annotation.HandleBeforeDelete;
import org.springframework.data.rest.core.annotation.HandleBeforeSave;
import org.springframework.data.rest.core.annotation.RepositoryEventHandler;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.migmak.planeverything.server.domain.Account;
import ru.migmak.planeverything.server.domain.ProjectMember;
import ru.migmak.planeverything.server.domain.Tag;
import ru.migmak.planeverything.server.exception.BadRequestException;
import ru.migmak.planeverything.server.exception.ServiceException;
import ru.migmak.planeverything.server.repository.AccountRepository;

import static ru.migmak.planeverything.server.domain.enums.PrivilegeCode.MANAGE_TAGS;

@Component
@RepositoryEventHandler
@RequiredArgsConstructor
public class TagHandler {

    private final AccountRepository accountRepository;

    @HandleBeforeCreate
    @HandleBeforeSave
    @HandleBeforeDelete
    @Transactional
    public void handleCreate(Tag tag) {
        if (tag.getProject() == null) {
            return;
        }
        Account currentAccount = accountRepository.findCurrentAccount()
                .orElseThrow(() -> new ServiceException("Current account not found"));
        ProjectMember member = tag.getProject().findMemberByAccountId(currentAccount.getId());
        if (!member.hasPrivilege(MANAGE_TAGS)) {
            throw new BadRequestException("You do not have privileges for this action");
        }
    }
}
