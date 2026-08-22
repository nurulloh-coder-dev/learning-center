package org.example.crm.projection;

import org.example.crm.entity.enums.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public interface  InvoiceProjection {
    String getId();
    String getInvoiceNumber();
    String getStudentId();
    String getStudentImageUrl();
    String getStudentUserId();
    String getStudentFullName();
    String getStudentPhone();
    LocalDate getStudentBirthDate();
    Role getStudentRole();
    String getParentPhone();
    BigDecimal getAmount();
    LocalDateTime getIssuedAt();
    InvoiceStatus getStatus();
}
