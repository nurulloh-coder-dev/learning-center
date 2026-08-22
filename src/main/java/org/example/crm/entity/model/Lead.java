package org.example.crm.entity.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.example.crm.entity.base.BaseEntity;
import org.example.crm.entity.enums.LeadSource;
import org.example.crm.entity.enums.LeadStatus;

import java.time.LocalDateTime;

@Entity
@Table(
        name = "lead",
        indexes = {
                @Index(name = "idx_lead_org_status_created", columnList = "organization_id, status, created_at DESC")
        }
)
@Setter
@Getter
@ToString
public class Lead extends BaseEntity {
    private String fullName;
    private String phone;
    @Enumerated(EnumType.STRING)
    private LeadStatus status = LeadStatus.NEW;
    @Enumerated(EnumType.STRING)
    private LeadSource source;
    private LocalDateTime callAt;
}
