package org.example.crm.service;

import org.example.crm.entity.dto.InvoiceCreateDto;
import org.example.crm.entity.dto.InvoiceDto;
import org.example.crm.entity.dto.InvoiceUpdateDto;
import org.example.crm.entity.enums.EnrollmentPaymentStatus;
import org.example.crm.entity.enums.InvoiceStatus;
import org.example.crm.entity.model.Enrollment;
import org.example.crm.entity.model.Invoice;
import org.example.crm.entity.model.Student;
import org.example.crm.mapper.InvoiceMapper;
import org.example.crm.projection.InvoiceProjection;
import org.example.crm.repository.EnrollmentRepository;
import org.example.crm.repository.InvoiceRepository;
import org.example.crm.repository.StudentRepository;
import org.example.crm.validator.InvoiceValidator;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
public class InvoiceService extends AbstractService<
        InvoiceRepository,
        InvoiceMapper,
        InvoiceValidator> implements CrudService<InvoiceCreateDto, InvoiceUpdateDto, InvoiceDto, String> {

    final StudentRepository studentRepository;
    final EnrollmentService enrollmentService;
    private final EnrollmentRepository enrollmentRepository;

    protected InvoiceService(InvoiceRepository repository, InvoiceMapper mapper, InvoiceValidator validator, StudentRepository studentRepository, EnrollmentService enrollmentService, EnrollmentRepository enrollmentRepository) {
        super(repository, mapper, validator);
        this.studentRepository = studentRepository;
        this.enrollmentService = enrollmentService;
        this.enrollmentRepository = enrollmentRepository;
    }

    private String wrapSearch(String search) {
        return search != null ? "%" + search.toLowerCase() + "%" : null;
    }

    @Override
    public Page<InvoiceDto> getAll(Pageable pageable, String search) {
        Page<InvoiceProjection> projectionPage = repository.
                getAllInvoicesByFilter(wrapSearch(search), null, null, null, pageable);
        return projectionPage.map(mapper::toDtoFromProjection);
    }

    @Override
    public InvoiceDto get(String id) {
        Invoice invoice = validator.validateIdAndGet(id);
        return mapper.toDto(invoice);
    }

    @Override
    public InvoiceDto create(InvoiceCreateDto createDto) {
        Invoice invoice = mapper.toEntity(createDto);

        Enrollment enrollment = invoice.getEnrollment();
        BigDecimal newPaidAmount = enrollment.getPaidAmount().add(invoice.getAmount());
        if (newPaidAmount.compareTo(enrollment.getMonthlyFee()) > 0) {
            enrollment.setStatus(EnrollmentPaymentStatus.PAID);
            BigDecimal monthlyFee = enrollment.getGroup().getLevel().getMonthlyFee();
            enrollment.setMonthlyFee(monthlyFee);
        }else {
            enrollment.setStatus(EnrollmentPaymentStatus.PARTIAL);
        }
        enrollment.setPaidAmount(newPaidAmount);
        enrollmentRepository.save(enrollment);


        return mapper.toDto(repository.save(invoice));
    }

    @Override
    public InvoiceDto update(InvoiceUpdateDto updateDto, String id) {
        return null;
    }

    @Override
    public void delete(String id) {
        Invoice invoice = validator.validateIdAndGet(id);
        invoice.setDeleted(true);
        repository.save(invoice);
    }

    public Page<InvoiceDto> getAllInvoices(String search, LocalDateTime from, LocalDateTime to,
                                           InvoiceStatus status, Pageable pageable) {


        from = from == null ? LocalDateTime.now().minusYears(4) : from;
        to = to == null ? LocalDateTime.now() : to;
        Page<InvoiceProjection> projectionPage = repository.getAllInvoicesByFilter(wrapSearch(search), from, to, status, pageable);
        return projectionPage.map(mapper::toDtoFromProjection);
    }

    public InvoiceDto returnInvoice(String studentId, String groupId) {

        Invoice invoice = mapper.toEntityReturn(studentId,groupId);
        return mapper.toDto(repository.save(invoice));
    }
}
