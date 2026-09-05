package org.example.crm.controller;

import jakarta.validation.Valid;
import org.example.crm.entity.dto.enrollment.EnrollmentCreateDto;
import org.example.crm.entity.dto.enrollment.EnrollmentDto;
import org.example.crm.entity.dto.enrollment.EnrollmentUpdateDto;
import org.example.crm.service.EnrollmentService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/enrollments")
public class EnrollmentController {

    private final EnrollmentService enrollmentService;

    public EnrollmentController(EnrollmentService enrollmentService) {
        this.enrollmentService = enrollmentService;
    }

    @GetMapping
    public ResponseEntity<Page<EnrollmentDto>> getAll(
            Pageable pageable,
            @RequestParam(required = false) String search,
            @RequestParam(required = false) String groupId
    ) {
        Page<EnrollmentDto> enrollments = groupId == null ? enrollmentService.getAll(pageable, search) : enrollmentService.getAll(pageable, search, groupId);
        return ResponseEntity.ok(enrollments);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EnrollmentDto> getById(@PathVariable String id) {
        EnrollmentDto enrollment = enrollmentService.get(id);
        return ResponseEntity.ok(enrollment);
    }

    @PostMapping
    public ResponseEntity<EnrollmentDto> create(@Valid @RequestBody EnrollmentCreateDto createDto) {
        EnrollmentDto createdEnrollment = enrollmentService.create(createDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdEnrollment);
    }


    @PutMapping("/{id}")
    public ResponseEntity<EnrollmentDto> update(
            @PathVariable String id,
            @Valid @RequestBody EnrollmentUpdateDto updateDto
    ) {
        EnrollmentDto updatedEnrollment = enrollmentService.update(updateDto, id);
        return ResponseEntity.ok(updatedEnrollment);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id, @RequestParam String reason) {
        enrollmentService.delete(id,reason);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/student/{studentId}")
    public ResponseEntity<List<EnrollmentDto>> getByStudentId(@PathVariable String studentId) {
        return ResponseEntity.ok(enrollmentService.getByStudentId(studentId));
    }
}