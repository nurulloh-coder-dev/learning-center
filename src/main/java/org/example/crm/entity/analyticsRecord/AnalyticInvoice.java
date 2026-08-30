package org.example.crm.entity.analyticsRecord;

public record AnalyticInvoice(
        Double invoiceAmount,
        Double invoiceAmountInAMonth,
        Double invoiceAmountInPreviousMonth,
        Double differenceInPercentage
) {
}
