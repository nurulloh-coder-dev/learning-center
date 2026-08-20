package org.example.crm.entity.model;

import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;
import org.example.crm.entity.base.BaseEntity;
import org.example.crm.entity.enums.LeadSource;
import org.example.crm.entity.enums.LeadStatus;

import java.time.LocalDateTime;

@Entity
@Setter
@Getter
public class Lead extends BaseEntity {
    private String fullName;
    private String phone;
    private LeadStatus status = LeadStatus.NEW;
    private LeadSource source;
    private LocalDateTime callAt;
}
