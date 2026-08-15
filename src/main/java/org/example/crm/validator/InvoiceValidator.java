package org.example.crm.validator;

import lombok.RequiredArgsConstructor;
import org.example.crm.exceptions.ErrorCodes;
import org.example.crm.exceptions.ErrorType;
import org.example.crm.entity.model.Invoice;
import org.example.crm.exceptions.RestException;
import org.example.crm.repository.InvoiceRepository;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class InvoiceValidator {
    final InvoiceRepository repository;

    public Invoice validateIdAndGet(String id) {
        return repository.findById(id)
                .orElseThrow(() -> new RestException(ErrorType.INVOICE_NOT_FOUND, ErrorCodes.NotFound));
    }
}
