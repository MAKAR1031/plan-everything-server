package ru.migmak.planeverything.server.resource.processor;


import lombok.RequiredArgsConstructor;
import org.springframework.data.rest.webmvc.support.RepositoryEntityLinks;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.ResourceProcessor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.migmak.planeverything.server.controller.AccountsController;
import ru.migmak.planeverything.server.domain.Account;
import ru.migmak.planeverything.server.domain.enums.AccountRoleCode;
import ru.migmak.planeverything.server.domain.projection.AccountWithRoleProjection;
import ru.migmak.planeverything.server.service.AccountService;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

@Component
@Transactional
@RequiredArgsConstructor
public class AccountProjectionResourceProcessor implements ResourceProcessor<Resource<AccountWithRoleProjection>> {

    private final AccountService accountService;
    private final RepositoryEntityLinks links;

    @Override
    public Resource<AccountWithRoleProjection> process(Resource<AccountWithRoleProjection> resource) {
        Account currentAccount = accountService.getCurrentAccount();
        if (!currentAccount.getRole().getCode().equals(AccountRoleCode.ADMIN.name())) {
            return resource;
        }

        Account account = accountService.findByLogin(resource.getContent().getLogin());
        if (!currentAccount.getId().equals(account.getId())) {
            if (account.isBlocked()) {
                resource.add(linkTo(methodOn(AccountsController.class)
                        .unlock(account.getId(), null))
                        .withRel("unlock"));
            } else {
                resource.add(linkTo(methodOn(AccountsController.class)
                        .lock(account.getId(), null))
                        .withRel("lock"));
            }
            resource.add(links.linkToSingleResource(Account.class, account.getId()).withRel("changeRole"));
        }
        return resource;
    }
}
