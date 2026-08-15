package org.example.learningcenter.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.learningcenter.exceptions.ErrorCodes;
import org.example.learningcenter.exceptions.ErrorType;
import org.example.learningcenter.entity.model.InvoiceCounter;
import org.example.learningcenter.exceptions.RestException;
import org.example.learningcenter.repository.InvoiceCounterRepository;
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
