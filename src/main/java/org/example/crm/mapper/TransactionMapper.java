package org.example.crm.mapper;

import lombok.RequiredArgsConstructor;
import org.example.crm.entity.dto.transaction.TransactionCreateDto;
import org.example.crm.entity.dto.transaction.TransactionDto;
import org.example.crm.entity.model.Invoice;
import org.example.crm.entity.model.Student;
import org.example.crm.entity.model.Transaction;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TransactionMapper {

    private final InvoiceMapper invoiceMapper;
    private final StudentMapper studentMapper;

    public Transaction toEntity(TransactionCreateDto createDto, Student student, Invoice invoice) {
        Transaction transaction = new Transaction();
        transaction.setType(createDto.type());
        transaction.setAmount(createDto.amount());
        transaction.setNote(createDto.note());
        transaction.setStudent(student);
        transaction.setInvoice(invoice);
        return transaction;
    }

    public TransactionDto toDto(Transaction transaction) {
        return new TransactionDto(
                transaction.getId(),
                transaction.getType(),
                transaction.getAmount(),
                transaction.getNote(),
                transaction.getInvoice() != null ? invoiceMapper.toDto(transaction.getInvoice()) : null,
                transaction.getStudent() != null ? studentMapper.toDto(transaction.getStudent()) : null,
                transaction.getCreatedAt()
        );
    }
}