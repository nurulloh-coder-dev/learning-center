package org.example.crm.entity.base;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import lombok.Getter;
import lombok.Setter;
import org.example.crm.config.CustomUserDetails;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

@Getter
@Setter
@MappedSuperclass
public abstract class TenantEntity extends BaseEntity {

    @Column(name = "organization_id", updatable = false, nullable = false)
    private String organizationId;

    @PrePersist
    public void setOrganizationIdOnPersist() {
        if (this.organizationId == null) {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            if (auth != null && auth.isAuthenticated() && !(auth instanceof AnonymousAuthenticationToken)) {
                if (auth.getPrincipal() instanceof CustomUserDetails custom) {
                    this.organizationId = custom.getOrganizationId();
                }
            }
        }
    }
}