package ru.migmak.planeverything.server.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "member_roles")
public class MemberRole {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 80)
    private String name;

    @ManyToMany
    @JoinTable(
            name = "member_role_privilege",
            joinColumns = @JoinColumn(name = "member_role_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "member_privilege_id", referencedColumnName = "id")
    )
    private List<Privilege> privilegeList;
}
