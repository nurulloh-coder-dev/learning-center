package org.example.crm.entity.enums;

public enum SubscriptionStatus {
    ACTIVE,
    GRACE,       // expired, still allowed in while you confirm the transfer
    EXPIRED,
    CANCELED
}