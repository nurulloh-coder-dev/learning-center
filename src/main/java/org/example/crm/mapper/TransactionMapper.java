package org.example.crm.mapper;

import lombok.RequiredArgsConstructor;
import org.example.crm.entity.dto.transaction.TransactionCreateDto;
import org.example.crm.entity.dto.transaction.TransactionDto;
import org.example.crm.entity.dto.transaction.TransactionUpdateDto;
import org.example.crm.entity.model.Transaction;
import org.example.crm.service.StudentService;
import org.example.crm.validator.InvoiceValidator;
import org.example.crm.validator.StudentValidator;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TransactionMapper {

    private final InvoiceMapper invoiceMapper;
    private final StudentMapper studentMapper;
    private final InvoiceValidator invoiceValidator;
    private final StudentValidator studentValidator;
    final StudentService studentService;

    public Transaction toEntity(TransactionCreateDto createDto) {
        Transaction transaction = new Transaction();
        transaction.setType(createDto.type());
        transaction.setAmount(createDto.amount());
        transaction.setInvoice(studentService.getLatestInvoice(createDto.studentId()));
        transaction.setUser(studentValidator.validateIdAndGet(createDto.studentId()));
        return transaction;
    }

    public TransactionDto toDto(Transaction transaction) {
        return new TransactionDto(
                transaction.getId(),
                transaction.getType(),
                transaction.getAmount(),
                transaction.getInvoice() != null ? invoiceMapper.toDto(transaction.getInvoice()) : null,
                transaction.getUser() != null ? studentMapper.toDto(transaction.getUser()) : null,
                transaction.getCreatedAt()
        );
    }

    public void mapUpdate(Transaction transaction, TransactionUpdateDto updateDto) {
        if (updateDto.type() != null) {
            transaction.setType(updateDto.type());
        }
        if (updateDto.amount() != null) {
            transaction.setAmount(updateDto.amount());
        }
        if (updateDto.invoiceId() != null) {
            transaction.setInvoice(invoiceValidator.validateIdAndGet(updateDto.invoiceId()));
        }
        if (updateDto.studentId() != null) {
            transaction.setUser(studentValidator.validateIdAndGet(updateDto.studentId()));
        }
    }
}