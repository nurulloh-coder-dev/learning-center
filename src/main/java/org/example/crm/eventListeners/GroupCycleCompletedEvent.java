package org.example.crm.eventListeners;

import java.math.BigDecimal;

public record GroupCycleCompletedEvent(String groupId, BigDecimal monthlyFee) {
}
