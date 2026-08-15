package org.example.crm.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.crm.exceptions.ErrorCodes;
import org.example.crm.exceptions.ErrorType;
import org.example.crm.entity.model.InvoiceCounter;
import org.example.crm.exceptions.RestException;
import org.example.crm.repository.InvoiceCounterRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class InvoiceNumberService {

    final InvoiceCounterRepository repository;


    @Transactional
    public String generateInvoiceNumber() {
        InvoiceCounter counter = repository.findByIdForUpdate("A")
                .orElseThrow(() ->  new RestException(ErrorType.INVOICE_COUNTER_NOT_FOUND, ErrorCodes.NotFound));
        Long nextNumber = counter.getLastNumber() + 1;
        counter.setLastNumber(nextNumber);
        repository.save(counter);

        return String.format("INV-A%07d", nextNumber);
    }

}
