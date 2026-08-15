package org.example.crm.controller;

import jakarta.validation.Valid;
import org.example.crm.entity.dto.attendance.AttendanceCreateDto;
import org.example.crm.entity.dto.attendance.AttendanceDto;
import org.example.crm.entity.dto.attendance.AttendanceUpdateDto;
import org.example.crm.service.AttendanceService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/attendance")
public class AttendanceController {

    private final AttendanceService service;

    public AttendanceController(AttendanceService attendanceService) {
        this.service = attendanceService;
    }

    @GetMapping
    public ResponseEntity<Page<AttendanceDto>> getAll(
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) String search
    ) {
        Pageable pageable = PageRequest.of(page, size);
        Page<AttendanceDto> attendances = service.getAll(pageable, search);
        return ResponseEntity.ok(attendances);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AttendanceDto> getById(@PathVariable String id) {
        AttendanceDto attendance = service.get(id);
        return ResponseEntity.ok(attendance);
    }

    @GetMapping("/student/{studentId}")
    public ResponseEntity<AttendanceDto> getByStudentId(@PathVariable String studentId) {
        AttendanceDto attendance = service.getByStudentId(studentId);
        return ResponseEntity.ok(attendance);
    }

    @GetMapping("/count")
    public ResponseEntity<Integer> getCount(){
        Integer count = service.getCount();
        return ResponseEntity.ok(count);
    }

    @PostMapping
    public ResponseEntity<AttendanceDto> create(@Valid @RequestBody AttendanceCreateDto createDto) {
        AttendanceDto createdAttendance = service.create(createDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdAttendance);
    }

    @PutMapping("/{id}")
    public ResponseEntity<AttendanceDto> update(
            @PathVariable String id,
            @Valid @RequestBody AttendanceUpdateDto updateDto
    ) {
        AttendanceDto updatedAttendance = service.update(updateDto, id);
        return ResponseEntity.ok(updatedAttendance);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}