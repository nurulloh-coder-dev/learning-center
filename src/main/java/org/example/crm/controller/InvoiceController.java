package org.example.crm.controller;

import lombok.RequiredArgsConstructor;
import org.example.crm.entity.dto.InvoiceCreateDto;
import org.example.crm.entity.dto.InvoiceDto;
import org.example.crm.entity.dto.InvoiceUpdateDto;
import org.example.crm.entity.enums.InvoiceStatus;
import org.example.crm.service.InvoiceService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("api/v1/invoice")
@RequiredArgsConstructor
public class InvoiceController {
    final InvoiceService service;

    @GetMapping
    public ResponseEntity<Page<InvoiceDto>> getAllInvoices(@RequestParam(required = false) String search,
                                                           @RequestParam(required = false) LocalDateTime from,
                                                           @RequestParam(required = false) LocalDateTime to,
                                                           @RequestParam(required = false) InvoiceStatus status,
                                                           @RequestParam(defaultValue = "0") Integer page,
                                                           @RequestParam(defaultValue = "20") Integer size) {
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(service.getAllInvoices(search, from, to, status, pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<InvoiceDto> getInvoiceById(@PathVariable String id) {
        return ResponseEntity.ok(service.get(id));
    }

    @PostMapping
    public ResponseEntity<InvoiceDto> createInvoice(@RequestBody InvoiceCreateDto createDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(createDto));
    }

    @PostMapping("/return")
    public ResponseEntity<InvoiceDto> returnInvoice(@RequestParam String studentId) {
        return ResponseEntity.ok(service.returnInvoice(studentId));
    }




    @PutMapping("/{id}")
    public ResponseEntity<InvoiceDto> updateInvoice(@PathVariable String id, @RequestBody InvoiceUpdateDto updateDto) {
        return ResponseEntity.ok(service.update(updateDto, id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteInvoice(@PathVariable String id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
