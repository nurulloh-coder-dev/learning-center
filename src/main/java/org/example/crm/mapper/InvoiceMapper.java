package org.example.crm.mapper;

import lombok.RequiredArgsConstructor;
import org.example.crm.entity.dto.InvoiceCreateDto;
import org.example.crm.entity.dto.InvoiceDto;
import org.example.crm.entity.dto.InvoiceUpdateDto;
import org.example.crm.entity.dto.enrollment.EnrollmentDto;
import org.example.crm.entity.enums.EnrollmentPaymentStatus;
import org.example.crm.entity.enums.InvoiceStatus;
import org.example.crm.entity.enums.InvoiceType;
import org.example.crm.entity.model.*;
import org.example.crm.exceptions.ErrorCodes;
import org.example.crm.exceptions.ErrorType;
import org.example.crm.exceptions.RestException;
import org.example.crm.projection.EnrollmentMonthlyFeeProjection;
import org.example.crm.projection.InvoiceProjection;
import org.example.crm.repository.EnrollmentRepository;
import org.example.crm.repository.GroupRepository;
import org.example.crm.service.EnrollmentService;
import org.example.crm.service.GroupService;
import org.example.crm.service.InvoiceNumberService;
import org.example.crm.service.LessonService;
import org.example.crm.validator.EnrollmentValidator;
import org.example.crm.validator.StudentValidator;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class InvoiceMapper {
    final InvoiceNumberService invoiceNumberService;
    final EnrollmentMapper enrollmentMapper;
    private final EnrollmentRepository enrollmentRepository;


    public Invoice toEntity(InvoiceCreateDto createDto) {
        Invoice invoice = new Invoice();
        invoice.setInvoiceNumber(invoiceNumberService.generateInvoiceNumber());
        invoice.setAmount(createDto.amount());
        invoice.setEnrollment(createDto.enrollment());
        invoice.setPaymentStatus(InvoiceStatus.PENDING);
        return invoice;
    }


    public InvoiceDto toDto(Invoice invoice) {
        return new InvoiceDto(
                invoice.getId(),
                invoice.getInvoiceNumber(),
                invoice.getAmount(),
                invoice.getCreatedAt(),
                invoice.getEnrollment() != null ? enrollmentMapper.toDto(invoice.getEnrollment()) : null

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
                        projection.getGroupId(),
                        projection.getReason()
                )
        );
    }

    public void mapUpdate(Invoice invoice, InvoiceUpdateDto updateDto) {
        if (updateDto.status() != null)
            invoice.setPaymentStatus(updateDto.status());
    }
}
