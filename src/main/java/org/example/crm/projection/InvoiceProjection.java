package org.example.crm.projection;

import org.example.crm.entity.enums.InvoiceStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public interface  InvoiceProjection {
    String getId();
    String getInvoiceNumber();
    String getEnrollmentId();
    String getStudentId();
    String getStudentFullName();
    String getGroupId();
    String getReason();
    BigDecimal getAmount();
    LocalDateTime getIssuedAt();
    InvoiceStatus getPaymentStatus();
}
