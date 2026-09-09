package org.example.crm.mapper;

import lombok.RequiredArgsConstructor;
import org.example.crm.entity.dto.InvoiceCreateDto;
import org.example.crm.entity.dto.InvoiceDto;
import org.example.crm.entity.dto.InvoiceUpdateDto;
import org.example.crm.entity.dto.enrollment.EnrollmentDto;
import org.example.crm.entity.enums.InvoiceStatus;
import org.example.crm.entity.model.*;
import org.example.crm.projection.InvoiceProjection;
import org.example.crm.service.InvoiceNumberService;
import org.example.crm.validator.EnrollmentValidator;
import org.example.crm.validator.GroupLevelValidator;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class InvoiceMapper {
    final InvoiceNumberService invoiceNumberService;
    final EnrollmentMapper enrollmentMapper;
    private final EnrollmentValidator enrollmentValidator;
    private final GroupLevelValidator groupLevelValidator;


    public Invoice toEntity(InvoiceCreateDto createDto) {
        Invoice invoice = new Invoice();
        invoice.setInvoiceNumber(invoiceNumberService.generateInvoiceNumber());
        invoice.setAmount(createDto.amount());
        invoice.setEnrollment(enrollmentValidator.validateIdAndGet(createDto.enrollmentId()));
        invoice.setLevel(groupLevelValidator.validateIdAndGetName(createDto.levelId()));
        invoice.setMonth(createDto.month());
        invoice.setPaymentStatus(InvoiceStatus.PENDING);
        return invoice;
    }


    public InvoiceDto toDto(Invoice invoice) {
        return new InvoiceDto(
                invoice.getId(),
                invoice.getInvoiceNumber(),
                invoice.getAmount(),
                invoice.getCreatedAt(),
                invoice.getEnrollment() != null ? enrollmentMapper.toDto(invoice.getEnrollment()) : null,
                invoice.getPaymentStatus()
        );
    }

    public InvoiceDto toDtoFromProjection(InvoiceProjection projection) {
        return new InvoiceDto(
                projection.getId(),
                projection.getInvoiceNumber(),
                projection.getAmount(),
                projection.getIssuedAt(),
                new EnrollmentDto(
                        projection.getEnrollmentId(),
                        projection.getStudentId(),
                        projection.getStudentFullName(),
                        projection.getGroupId(),
                        projection.getReason()
                ),
                projection.getPaymentStatus()
        );
    }

    public void mapUpdate(Invoice invoice, InvoiceUpdateDto updateDto) {
        if (updateDto.status() != null)
            invoice.setPaymentStatus(updateDto.status());
    }
}
