package org.example.crm.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.crm.entity.dto.transaction.TransactionCreateDto;
import org.example.crm.entity.dto.transaction.TransactionDto;
import org.example.crm.filters.TransactionFilterDto;
import org.example.crm.entity.dto.transaction.TransactionUpdateDto;
import org.example.crm.entity.enums.TransactionType;
import org.example.crm.service.TransactionService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/transaction")
public class TransactionController {

    private final TransactionService transactionService;

    @GetMapping
    public ResponseEntity<Page<TransactionDto>> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "10") TransactionType type,
            @RequestParam(required = false) String search
    ) {
        Pageable pageable = PageRequest.of(page, size);
        Page<TransactionDto> transactions = transactionService.getAll(pageable, new TransactionFilterDto(type,search));
        return ResponseEntity.ok(transactions);
    }

    @GetMapping("/count")
    public ResponseEntity<Map<String, Long>> count() {
        Long count = transactionService.getAllCount();
        return ResponseEntity.ok(Map.of("count", count));
    }

    @GetMapping("/{id}")
    public ResponseEntity<TransactionDto> getById(@PathVariable String id) {
        TransactionDto transaction = transactionService.get(id);
        return ResponseEntity.ok(transaction);
    }

    @PostMapping
    public ResponseEntity<TransactionDto> create(@Valid @RequestBody TransactionCreateDto createDto) {
        TransactionDto createdTransaction = transactionService.create(createDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdTransaction);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TransactionDto> update(
            @PathVariable String id,
            @Valid @RequestBody TransactionUpdateDto updateDto
    ) {
        TransactionDto updatedTransaction = transactionService.update(updateDto, id);
        return ResponseEntity.ok(updatedTransaction);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        transactionService.delete(id);
        return ResponseEntity.noContent().build();
    }
}