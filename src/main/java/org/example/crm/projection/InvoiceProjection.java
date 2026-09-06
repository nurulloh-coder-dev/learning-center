package org.example.crm.projection;

import org.example.crm.entity.enums.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public interface  InvoiceProjection {
    String getId();
    String getInvoiceNumber();
    String getEnrollmentId();
    String getStudentId();
    String getGroupId();
    String getReason();
    BigDecimal getMonthlyFee();
    BigDecimal getPaidAmount();
    EnrollmentPaymentStatus getEnrollmentStatus();

    BigDecimal getAmount();
    LocalDateTime getIssuedAt();
    InvoiceType getType();
}
