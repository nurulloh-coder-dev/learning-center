package org.example.crm.projection;

public interface AnalyticInvoiceProjection {
    Double getInvoiceAmount();
    Double getInvoiceAmountInMonth();
    Double getInvoiceAmountInPreviousMonth();
}
