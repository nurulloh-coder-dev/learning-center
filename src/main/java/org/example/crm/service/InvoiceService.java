package org.example.crm.service;

import jakarta.transaction.Transactional;
import org.example.crm.entity.dto.InvoiceCreateDto;
import org.example.crm.entity.dto.InvoiceDto;
import org.example.crm.entity.dto.InvoiceUpdateDto;
import org.example.crm.entity.dto.transaction.TransactionCreateDto;
import org.example.crm.entity.enums.InvoiceStatus;
import org.example.crm.entity.enums.TransactionType;
import org.example.crm.entity.model.Enrollment;
import org.example.crm.entity.model.Group;
import org.example.crm.entity.model.Invoice;
import org.example.crm.exceptions.ErrorCodes;
import org.example.crm.exceptions.ErrorType;
import org.example.crm.exceptions.RestException;
import org.example.crm.mapper.InvoiceMapper;
import org.example.crm.projection.InvoiceProjection;
import org.example.crm.repository.EnrollmentRepository;
import org.example.crm.repository.InvoiceRepository;
import org.example.crm.repository.StudentRepository;
import org.example.crm.validator.GroupValidator;
import org.example.crm.validator.InvoiceValidator;
import org.example.crm.validator.UserValidator;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class InvoiceService extends AbstractService<
        InvoiceRepository,
        InvoiceMapper,
        InvoiceValidator> implements CrudService<InvoiceCreateDto, InvoiceUpdateDto, InvoiceDto, String> {

    final StudentRepository studentRepository;
    final EnrollmentService enrollmentService;
    private final EnrollmentRepository enrollmentRepository;
    private final UserValidator userValidator;
    private final TransactionService transactionService;
    private final GroupValidator groupValidator;

    protected InvoiceService(InvoiceRepository repository, InvoiceMapper mapper, InvoiceValidator validator, StudentRepository studentRepository, EnrollmentService enrollmentService, EnrollmentRepository enrollmentRepository, UserValidator userValidator, TransactionService transactionService, GroupValidator groupValidator) {
        super(repository, mapper, validator);
        this.studentRepository = studentRepository;
        this.enrollmentService = enrollmentService;
        this.enrollmentRepository = enrollmentRepository;
        this.userValidator = userValidator;
        this.transactionService = transactionService;
        this.groupValidator = groupValidator;
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
        Invoice entity = mapper.toEntity(createDto);
        return mapper.toDto(repository.save(entity));
    }

    @Override
    public InvoiceDto update(InvoiceUpdateDto updateDto, String id) {
        return null;
    }

    @Override
    public void delete(String id) {
        String organizationId = userValidator.authenticateAndGetOrganizationId();
        validator.validateId(id, organizationId);
        repository.softDelete(id);
    }

    public Page<InvoiceDto> getAllInvoices(String search, LocalDateTime from, LocalDateTime to,
                                           InvoiceStatus status, Pageable pageable) {
        from = from == null ? LocalDateTime.now().minusYears(4) : from;
        to = to == null ? LocalDateTime.now() : to;
        Page<InvoiceProjection> projectionPage = repository.getAllInvoicesByFilter(wrapSearch(search), from, to, status, pageable);
        return projectionPage.map(mapper::toDtoFromProjection);
    }


    @Transactional
    public void createGroupInvoice(String groupId) {
        Group group = groupValidator.validateIdAndGet(groupId);
        BigDecimal monthlyFee = group.getLevel().getMonthlyFee();
        if (monthlyFee == null || monthlyFee.compareTo(BigDecimal.ZERO) <= 0) {
            throw new RestException(ErrorType.INVALID_INPUT, ErrorCodes.BadRequest);
        }
        String organizationId = userValidator.authenticateAndGetOrganizationId();
        if (!group.getOrganizationId().equals(organizationId)) {
            throw new RestException(ErrorType.FORBIDDEN, ErrorCodes.Forbidden);
        }
        String levelName = group.getLevel().getName();
        boolean alreadyCreated = repository.checkIfAlreadyCreated(groupId, levelName, group.getCurrentMonth());
        if (alreadyCreated){
            throw new RestException(ErrorType.INVOICE_ALREADY_CREATED,ErrorCodes.AlreadyExists);
        }
        List<Enrollment> enrollments = enrollmentRepository.getAllByGroupId(groupId);
        if (enrollments.isEmpty()) {
            throw new RestException(ErrorType.ENROLLMENT_NOT_FOUND, ErrorCodes.NotFound);
        }

        List<Invoice> invoices = enrollments.stream()
                .map(e -> mapper.toEntity(new InvoiceCreateDto(e.getId(), monthlyFee, levelName, group.getCurrentMonth())))
                .toList();
        repository.saveAll(invoices);

        BigDecimal feeDebit = monthlyFee.negate();
        for (Enrollment enrollment : enrollments) {
            transactionService.create(new TransactionCreateDto(
                    TransactionType.MONTHLY_FEE,
                    feeDebit,
                    enrollment.getStudent().getId()
            ));
        }
    }
}
