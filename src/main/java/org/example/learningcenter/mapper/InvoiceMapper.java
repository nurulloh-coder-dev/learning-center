package org.example.learningcenter.mapper;

import lombok.RequiredArgsConstructor;
import org.example.learningcenter.entity.dto.InvoiceCreateDto;
import org.example.learningcenter.entity.dto.InvoiceDto;
import org.example.learningcenter.entity.dto.InvoiceUpdateDto;
import org.example.learningcenter.entity.dto.group.GroupDto;
import org.example.learningcenter.entity.dto.student.StudentDto;
import org.example.learningcenter.entity.dto.teacher.TeacherDto;
import org.example.learningcenter.entity.dto.timeTable.TimeTableDto;
import org.example.learningcenter.entity.dto.user.UserDto;
import org.example.learningcenter.entity.enums.InvoiceStatus;
import org.example.learningcenter.entity.model.Invoice;
import org.example.learningcenter.entity.model.Student;
import org.example.learningcenter.projection.InvoiceProjection;
import org.example.learningcenter.service.InvoiceNumberService;
import org.example.learningcenter.validator.StudentValidator;
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
}
