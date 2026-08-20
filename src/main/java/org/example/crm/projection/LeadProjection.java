package org.example.crm.projection;

import org.example.crm.entity.enums.LeadSource;
import org.example.crm.entity.enums.LeadStatus;

import java.time.LocalDateTime;

public interface LeadProjection {
    String getId();
    String getFullName();
    String getPhone();
    LeadStatus getStatus();
    LocalDateTime getCallAt();
    LeadSource getSource();
}
