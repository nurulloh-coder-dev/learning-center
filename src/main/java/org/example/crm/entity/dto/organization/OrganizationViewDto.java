package org.example.crm.entity.dto.organization;

import org.example.crm.entity.enums.Role;
import org.example.crm.entity.enums.SubscriptionStatus;

public record OrganizationViewDto(String id, String name, Role role, SubscriptionStatus subscriptionStatus) {
}
