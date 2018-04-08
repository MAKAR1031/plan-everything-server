package ru.migmak.planeverything.server.domain;

import lombok.Getter;
import lombok.Setter;
import ru.migmak.planeverything.server.domain.enums.PrivilegeCode;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "members")
public class ProjectMember {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "project_id", nullable = false)
    private Project project;

    @ManyToOne
    @JoinColumn(name = "account_id", nullable = false)
    private Account account;

    @ManyToOne
    @JoinColumn(name = "member_role_id", nullable = false)
    private MemberRole role;

    public boolean hasPrivilege(PrivilegeCode code) {
        return role.getPrivilegeList().stream().anyMatch(privilege -> privilege.getCode().equals(code.name()));
    }
}
