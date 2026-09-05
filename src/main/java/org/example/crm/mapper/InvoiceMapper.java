package org.example.crm.mapper;

import lombok.RequiredArgsConstructor;
import org.example.crm.entity.dto.InvoiceCreateDto;
import org.example.crm.entity.dto.InvoiceDto;
import org.example.crm.entity.dto.InvoiceUpdateDto;
import org.example.crm.entity.dto.enrollment.EnrollmentDto;
import org.example.crm.entity.enums.InvoiceStatus;
import org.example.crm.entity.enums.InvoiceType;
import org.example.crm.entity.model.*;
import org.example.crm.exceptions.ErrorCodes;
import org.example.crm.exceptions.ErrorType;
import org.example.crm.exceptions.RestException;
import org.example.crm.projection.InvoiceProjection;
import org.example.crm.repository.GroupRepository;
import org.example.crm.service.EnrollmentService;
import org.example.crm.service.GroupService;
import org.example.crm.service.InvoiceNumberService;
import org.example.crm.service.LessonService;
import org.example.crm.validator.StudentValidator;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class InvoiceMapper {
    final InvoiceNumberService invoiceNumberService;
    private final StudentValidator studentValidator;
    final EnrollmentService enrollmentService;
    final EnrollmentMapper enrollmentMapper;
    final GroupService groupService;
    final LessonService lessonService;
    private final GroupRepository groupRepository;


    public Invoice toEntity(InvoiceCreateDto createDto) {
        Invoice invoice = new Invoice();
        invoice.setInvoiceNumber(invoiceNumberService.generateInvoiceNumber());
        invoice.setAmount(createDto.amount());
        invoice.setIssuedAt(LocalDateTime.now());
        invoice.setEnrollment(enrollmentService.getEnrollmentByStudentGroup(createDto.studentId(), createDto.groupId()));
        return invoice;
    }


    public InvoiceDto toDto(Invoice invoice) {
        return new InvoiceDto(
                invoice.getId(),
                invoice.getInvoiceNumber(),
                invoice.getAmount(),
                invoice.getIssuedAt(),
                invoice.getType(),
                invoice.getEnrollment() != null ? enrollmentMapper.toDto(invoice.getEnrollment()) : null

        );
    }

    public InvoiceDto toDtoFromProjection(InvoiceProjection projection) {
        return new InvoiceDto(
                projection.getId(),
                projection.getInvoiceNumber(),
                projection.getAmount(),
                projection.getIssuedAt(),
                projection.getType(),
                new EnrollmentDto(
                        projection.getEnrollmentId(),
                        projection.getStudentId(),
                        projection.getGroupId(),
                        projection.getReason(),
                        projection.getMonthlyFee(),
                        projection.getPaidAmount(),
                        projection.getEnrollmentStatus()
                )
        );
    }

    public void mapUpdate(Invoice invoice, InvoiceUpdateDto updateDto) {
        if (updateDto.status() != null)
            invoice.setPaymentStatus(updateDto.status());
    }


    public Invoice toEntityReturn(String studentId, String groupId) {
        studentValidator.validateIdAndGet(studentId);
        Group group = groupRepository.findById(groupId).orElseThrow(() -> new RestException(ErrorType.GROUP_NOT_FOUND, ErrorCodes.NotFound));
        Enrollment enrollment = enrollmentService.getEnrollmentByStudentGroup(studentId, groupId);

        Invoice invoice = new Invoice();
        invoice.setInvoiceNumber(invoiceNumberService.generateInvoiceNumber());
        invoice.setPaymentStatus(InvoiceStatus.PAID);
        invoice.setType(InvoiceType.RETURNED);
        invoice.setIssuedAt(LocalDateTime.now());
        invoice.setEnrollment(enrollment);
        Level level = group.getLevel();


        Integer lessonCount = lessonService.getLessonCountByGroup(groupId, level.getName());
        BigDecimal moneyPerLesson = level.getMonthlyFee().divide(BigDecimal
                .valueOf(level.getLessonCount()),0, RoundingMode.HALF_UP);
        BigDecimal multiply = moneyPerLesson.multiply(BigDecimal.valueOf(lessonCount));
        BigDecimal subtract = enrollment.getPaidAmount().subtract(multiply);
        if (subtract.compareTo(BigDecimal.ZERO) < 0) {
            throw new RestException(ErrorType.ZERO_ON_BALANCE, ErrorCodes.BadRequest);
        }

        invoice.setAmount(subtract);
        return invoice;
    }
}
