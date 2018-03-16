package ru.migmak.planeverything.server.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "accounts")
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String fullName;

    @Column(nullable = false, unique = true)
    private String externalId;

    @Column(nullable = false)
    private boolean blocked;

    @ManyToOne
    @JoinColumn(name = "account_role_id", nullable = false)
    private AccountRole role;
}
