package org.example.crm.entity.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.example.crm.entity.base.BaseEntity;
import org.example.crm.entity.enums.AdministratorPermission;
import org.example.crm.entity.enums.Role;

import java.util.List;

@Entity
@Table(
        name = "user_organizations",
        indexes = {
                @Index(name = "idx_user_org_tenant", columnList = "organization_id, deleted"),
                @Index(name = "idx_user_org_branch", columnList = "branch_id")
        }
)
@Getter
@Setter
public class UserOrganization extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "organization_id", nullable = false)
    private Organization organization;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "branch_id")
    private Branch branch;

    @ElementCollection
    @Enumerated(EnumType.STRING)
    private List<AdministratorPermission> permissions;

    @Column(nullable = false)
    private boolean active = true;
}