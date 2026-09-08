package org.example.crm.service;

import jakarta.transaction.Transactional;
import org.example.crm.entity.dto.transaction.TransactionCreateDto;
import org.example.crm.entity.dto.transaction.TransactionDto;
import org.example.crm.entity.dto.transaction.TransactionUpdateDto;
import org.example.crm.entity.model.Transaction;
import org.example.crm.exceptions.ErrorCodes;
import org.example.crm.exceptions.ErrorType;
import org.example.crm.exceptions.RestException;
import org.example.crm.mapper.TransactionMapper;
import org.example.crm.repository.StudentRepository;
import org.example.crm.repository.TransactionRepository;
import org.example.crm.validator.OrganizationValidator;
import org.example.crm.validator.TransactionValidator;
import org.example.crm.validator.UserValidator;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class TransactionService extends AbstractService<
        TransactionRepository,
        TransactionMapper,
        TransactionValidator> implements CrudService<TransactionCreateDto, TransactionUpdateDto, TransactionDto, String> {

    private final UserValidator userValidator;
    private final OrganizationValidator organizationValidator;
    private final StudentRepository studentRepository;

    protected TransactionService(TransactionRepository repository, TransactionMapper mapper, TransactionValidator validator, UserValidator userValidator, OrganizationValidator organizationValidator, StudentRepository studentRepository) {
        super(repository, mapper, validator);
        this.userValidator = userValidator;
        this.organizationValidator = organizationValidator;
        this.studentRepository = studentRepository;
    }

    @Override
    public Page<TransactionDto> getAll(Pageable pageable, String search) {
        String organizationId = userValidator.authenticateAndGetOrganizationId();
        Page<Transaction> transactions = repository.findAll(search, organizationId, pageable);
        return transactions.map(mapper::toDto);
    }

    @Override
    public TransactionDto get(String id) {
        Transaction transaction = validator.validateIdAndGet(id);
        String organizationId = userValidator.authenticateAndGetOrganizationId();
        organizationValidator.validateOrganizationMatch(transaction.getOrganizationId(), organizationId);
        return mapper.toDto(transaction);
    }

    @Override
    public TransactionDto create(TransactionCreateDto createDto) {
        Transaction transaction = mapper.toEntity(createDto);
        String organizationId = userValidator.authenticateAndGetOrganizationId();
        organizationValidator.validateOrganizationMatch(transaction.getInvoice().getOrganizationId(), organizationId);
        Transaction save = repository.save(transaction);
        studentRepository.setNewBalance(save.getAmount(),createDto.studentId());
        return mapper.toDto(save);
    }

    @Override
    @Transactional
    public TransactionDto update(TransactionUpdateDto updateDto, String id) {
        Transaction transaction = validator.validateIdAndGet(id);
        String organizationId = userValidator.authenticateAndGetOrganizationId();
        organizationValidator.validateOrganizationMatch(transaction.getOrganizationId(), organizationId);
        mapper.mapUpdate(transaction, updateDto);
        Transaction save = repository.save(transaction);
        return mapper.toDto(save);
    }

    @Override
    public void delete(String id) {
        validator.validateId(id);
        String organizationId = userValidator.authenticateAndGetOrganizationId();
        int rowsUpdated = repository.deleteByIdFalse(id, organizationId);
        if (rowsUpdated == 0) {
            throw new RestException(ErrorType.FORBIDDEN, ErrorCodes.Unauthorized);
        }
    }

    public Long getAllCount() {
        String organizationId = userValidator.authenticateAndGetOrganizationId();
        return repository.countByOrganizationId(organizationId);
    }
}