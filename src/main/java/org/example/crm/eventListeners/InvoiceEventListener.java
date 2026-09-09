package org.example.crm.eventListeners;

import lombok.AllArgsConstructor;
import org.example.crm.service.InvoiceService;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@AllArgsConstructor
public class InvoiceEventListener {

    private final InvoiceService invoiceService;

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleGroupBilling(GroupCycleCompletedEvent event) {
        invoiceService.createGroupInvoice(event.groupId());
    }
}