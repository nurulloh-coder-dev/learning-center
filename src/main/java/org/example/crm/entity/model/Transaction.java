package org.example.crm.entity.model;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.crm.entity.base.BaseEntity;
import org.example.crm.entity.enums.InvoiceStatus;
import org.example.crm.entity.enums.TransactionType;

import java.math.BigDecimal;

@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Transaction extends BaseEntity {

    private TransactionType type;

    private BigDecimal amount;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private Invoice invoice;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private Student user;

}
