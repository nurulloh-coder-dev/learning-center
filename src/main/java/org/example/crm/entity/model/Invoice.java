package org.example.crm.entity.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import org.example.crm.entity.base.BaseEntity;
import org.example.crm.entity.enums.InvoiceStatus;
import org.example.crm.entity.enums.InvoiceType;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "invoices")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Invoice extends BaseEntity {


    @Column(unique = true, nullable = false)
    private String invoiceNumber;

    @Column(nullable = false)
    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    private InvoiceStatus paymentStatus;

    private LocalDateTime issuedAt = LocalDateTime.now();

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "student_id")
    private Student student;


    @Enumerated(EnumType.STRING)
    private InvoiceType type;
}