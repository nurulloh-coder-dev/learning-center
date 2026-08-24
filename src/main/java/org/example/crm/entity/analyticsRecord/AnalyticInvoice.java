package org.example.crm.entity.analyticsRecord;

public record AnalyticInvoice(
        Double invoiceAmount,
        Integer invoiceAmountInAMonth
) {
}
