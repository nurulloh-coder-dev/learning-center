package org.example.crm.validator;

import lombok.RequiredArgsConstructor;
import org.example.crm.entity.enums.InvoiceStatus;
import org.example.crm.entity.model.Transaction;
import org.example.crm.exceptions.ErrorCodes;
import org.example.crm.exceptions.ErrorType;
import org.example.crm.exceptions.RestException;
import org.example.crm.repository.TransactionRepository;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TransactionValidator {

    private final TransactionRepository transactionRepository;

    public Transaction validateIdAndGet(String id) {
        return transactionRepository.findById(id)
                .orElseThrow(() -> new RestException(ErrorType.TRANSACTION_NOT_FOUND, ErrorCodes.NotFound));
    }

    public void validateId(String id) {
        Boolean exists = transactionRepository.checkId(id).orElse(false);
        if (!exists) {
            throw new RestException(ErrorType.TRANSACTION_NOT_FOUND, ErrorCodes.NotFound);
        }
    }

    public void validate(Transaction transaction) {
        if (transaction.getInvoice().getPaymentStatus().equals(InvoiceStatus.PAID)){
            throw new RestException(ErrorType.INVOICE_ALREADY_PAID,ErrorCodes.BadRequest);
        }
    }
}