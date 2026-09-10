package org.example.crm.service;

import jakarta.transaction.Transactional;
import org.example.crm.entity.dto.transaction.TransactionCreateDto;
import org.example.crm.entity.dto.transaction.TransactionDto;
import org.example.crm.filters.TransactionFilterDto;
import org.example.crm.entity.dto.transaction.TransactionUpdateDto;
import org.example.crm.entity.enums.TransactionType;
import org.example.crm.entity.model.Invoice;
import org.example.crm.entity.model.Student;
import org.example.crm.entity.model.Transaction;
import org.example.crm.exceptions.ErrorCodes;
import org.example.crm.exceptions.ErrorType;
import org.example.crm.exceptions.RestException;
import org.example.crm.mapper.TransactionMapper;
import org.example.crm.repository.StudentRepository;
import org.example.crm.repository.TransactionRepository;
import org.example.crm.specs.TransactionSpec;
import org.example.crm.validator.InvoiceValidator;
import org.example.crm.validator.OrganizationValidator;
import org.example.crm.validator.TransactionValidator;
import org.example.crm.validator.UserValidator;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
public class TransactionService extends AbstractService<
        TransactionRepository,
        TransactionMapper,
        TransactionValidator> implements CrudService<TransactionFilterDto, TransactionCreateDto, TransactionUpdateDto, TransactionDto, String, Page<TransactionDto>> {

    private final UserValidator userValidator;
    private final OrganizationValidator organizationValidator;
    private final StudentRepository studentRepository;
    private final InvoiceValidator invoiceValidator;

    protected TransactionService(TransactionRepository repository, TransactionMapper mapper, TransactionValidator validator, UserValidator userValidator, OrganizationValidator organizationValidator, StudentRepository studentRepository, InvoiceValidator invoiceValidator) {
        super(repository, mapper, validator);
        this.userValidator = userValidator;
        this.organizationValidator = organizationValidator;
        this.studentRepository = studentRepository;
        this.invoiceValidator = invoiceValidator;
    }

    @Override
    public Page<TransactionDto> getAll(Pageable pageable, TransactionFilterDto filterDto) {
        String orgId = userValidator.authenticateAndGetOrganizationId();
        Specification<Transaction> spec = Specification
                .where(TransactionSpec.isNotDeleted())
                .and(TransactionSpec.belongsToOrg(orgId))
                .and(TransactionSpec.hasType(filterDto.type()))
                .and(TransactionSpec.searchKeyword(filterDto.search()));

        return repository.findAll(spec, pageable).map(mapper::toDto);
    }

    @Override
    public TransactionDto get(String id) {
        Transaction transaction = validator.validateIdAndGet(id);
        String organizationId = userValidator.authenticateAndGetOrganizationId();
        organizationValidator.validateOrganizationMatch(transaction.getOrganizationId(), organizationId);
        return mapper.toDto(transaction);
    }

    @Override
    @Transactional
    public TransactionDto create(TransactionCreateDto createDto) {
        String currentOrgId = userValidator.authenticateAndGetOrganizationId();
        Student student = studentRepository.findById(createDto.studentId())
                .orElseThrow(() -> new RestException(ErrorType.STUDENT_NOT_FOUND, ErrorCodes.NotFound));
        organizationValidator.validateOrganizationMatch(student.getUser().getOrganizationId(), currentOrgId);

        Invoice invoice = resolveInvoiceIfRequired(createDto, currentOrgId);

        Transaction transaction = mapper.toEntity(createDto, student, invoice);
        validator.validate(transaction);

        Transaction saved = repository.save(transaction);
        studentRepository.setNewBalance(saved.getAmount(), student.getId());

        return mapper.toDto(saved);
    }

    private Invoice resolveInvoiceIfRequired(TransactionCreateDto dto, String currentOrgId) {
        if (dto.type() != TransactionType.MONTHLY_FEE) {
            return null;
        }
        if (dto.invoiceId() == null || dto.invoiceId().isBlank()) {
            throw new RestException(ErrorType.INVOICE_REQUIRED, ErrorCodes.BadRequest);
        }
        Invoice invoice = invoiceValidator.validateIdAndGet(dto.invoiceId());
        organizationValidator.validateOrganizationMatch(invoice.getOrganizationId(), currentOrgId);
        return invoice;
    }


    public void internalCreate(Transaction transaction) {
        Transaction save = repository.save(transaction);
        studentRepository.setNewBalance(save.getAmount(), transaction.getStudent().getId());
    }

    @Override
    public TransactionDto update(TransactionUpdateDto updateDto, String id) {
        return null;
    }

    @Override
    public void delete(String id) {

    }

    public Long getAllCount() {
        String organizationId = userValidator.authenticateAndGetOrganizationId();
        return repository.countByOrganizationId(organizationId);
    }
}