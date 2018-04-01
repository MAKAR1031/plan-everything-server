package ru.migmak.planeverything.server.domain;


import lombok.Getter;
import lombok.Setter;
import ru.migmak.planeverything.server.exception.NotFoundException;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "projects")
public class Project {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 120)
    private String name;

    private String description;

    @Column(name = "create_date", nullable = false)
    @Temporal(TemporalType.DATE)
    private Date createDate;

    @Column(nullable = false)
    private boolean opened;

    @ManyToOne
    @JoinColumn(name = "account_author_id", nullable = false)
    private Account author;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "project")
    private List<ProjectMember> members;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "project")
    private List<Task> tasks;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "project")
    private List<Tag> tags;

    public void addMember(ProjectMember member) {
        if (this.members == null) {
            members = new ArrayList<>();
        }
        members.add(member);
    }

    public ProjectMember findMemberByAccountId(Long id) {
        return this.members
                .stream()
                .filter(member -> id.equals(member.getAccount().getId()))
                .findFirst()
                .orElseThrow(NotFoundException::new);
    }
}
