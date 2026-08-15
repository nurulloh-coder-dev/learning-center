package org.example.crm.mapper;

import lombok.RequiredArgsConstructor;
import org.example.crm.entity.dto.InvoiceCreateDto;
import org.example.crm.entity.dto.InvoiceDto;
import org.example.crm.entity.dto.InvoiceUpdateDto;
import org.example.crm.entity.dto.student.StudentDto;
import org.example.crm.entity.dto.user.UserDto;
import org.example.crm.entity.enums.InvoiceStatus;
import org.example.crm.entity.model.Invoice;
import org.example.crm.entity.model.Student;
import org.example.crm.projection.InvoiceProjection;
import org.example.crm.service.InvoiceNumberService;
import org.example.crm.validator.StudentValidator;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class InvoiceMapper {
    final StudentMapper studentMapper;
    final InvoiceNumberService invoiceNumberService;
    private final StudentValidator studentValidator;

    public Invoice toEntity(InvoiceCreateDto createDto) {
        Invoice invoice = new Invoice();
        invoice.setInvoiceNumber(invoiceNumberService.generateInvoiceNumber());
        invoice.setAmount(createDto.amount());
        invoice.setPaymentStatus(InvoiceStatus.PENDING);
        invoice.setIssuedAt(LocalDateTime.now());
        Student student = studentValidator.validateIdAndGet(createDto.studentId());
        invoice.setStudent(student);
        return invoice;
    }


    public InvoiceDto toDto(Invoice invoice) {
        return new InvoiceDto(
                invoice.getId(),
                invoice.getInvoiceNumber(),
                studentMapper.toDto(invoice.getStudent()),
                invoice.getAmount(),
                invoice.getIssuedAt(),
                invoice.getPaymentStatus()
        );
    }

    public InvoiceDto toDtoFromProjection(InvoiceProjection projection) {
        return new InvoiceDto(
                projection.getId(),
                projection.getInvoiceNumber(),
                new StudentDto(
                        projection.getStudentId(),
                        new UserDto(
                                projection.getStudentUserId(),
                                projection.getStudentImageUrl(),
                                projection.getStudentFullName(),
                                projection.getStudentPhone(),
                                projection.getStudentBirthDate(),
                                projection.getStudentRole()
                        ),
                        projection.getParentPhone()
                ),
                projection.getAmount(),
                projection.getIssuedAt(),
                projection.getStatus()
        );
    }

    public void mapUpdate(Invoice invoice, InvoiceUpdateDto updateDto) {
        if (updateDto.status() != null)
            invoice.setPaymentStatus(updateDto.status());
    }


    public Invoice toEntityReturn(String studentId) {
        Student student = studentValidator.validateIdAndGet(studentId);
        Invoice invoice = new Invoice();
        invoice.setInvoiceNumber(invoiceNumberService.generateInvoiceNumber());
        invoice.setAmount(student.getBalance());
        invoice.setPaymentStatus(InvoiceStatus.PAID);
        invoice.setType(InvoiceType.RETURNED);
        invoice.setIssuedAt(LocalDateTime.now());
        invoice.setStudent(student);
        return invoice;
    }
}
