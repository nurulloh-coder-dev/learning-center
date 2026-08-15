package org.example.crm.controller;

import jakarta.validation.Valid;
import org.example.crm.entity.dto.student.StudentCreateDto;
import org.example.crm.entity.dto.student.StudentDto;
import org.example.crm.entity.dto.student.StudentUpdateDto;
import org.example.crm.service.StudentService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("api/v1/student")
public class StudentController {

    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @GetMapping
    public ResponseEntity<Page<StudentDto>> getAll(
            Pageable pageable,
            @RequestParam(required = false) String search,
            @RequestParam String organizationId
    ) {
        Page<StudentDto> students = studentService.getAll(pageable, search, organizationId);
        return ResponseEntity.ok(students);
    }

    @GetMapping("/count")
    public ResponseEntity<Map<String, Long>> count() {
        return ResponseEntity.ok(Map.of("count", studentService.getAllCount()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<StudentDto> getById(@PathVariable String id) {
        StudentDto student = studentService.get(id);
        return ResponseEntity.ok(student);
    }

    @GetMapping("/phone")
    public ResponseEntity<List<StudentDto>> getByPhone(@RequestParam String phone) {
        List<StudentDto> student = studentService.getByPhone(phone);
        return ResponseEntity.ok(student);
    }

    @GetMapping("{groupId}/students")
    public ResponseEntity<List<StudentDto>> getStudentsByGroupId(@PathVariable String groupId) {
        List<StudentDto> studentsByGroupId = studentService.getStudentsByGroupId(groupId);
        return ResponseEntity.ok(studentsByGroupId);
    }

    @PostMapping
    public ResponseEntity<StudentDto> create(@Valid @RequestBody StudentCreateDto createDto) {
        StudentDto createdStudent = studentService.create(createDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdStudent);
    }

    @PutMapping("/{id}")
    public ResponseEntity<StudentDto> update(
            @PathVariable String id,
            @Valid @RequestBody StudentUpdateDto updateDto
    ) {
        StudentDto updatedStudent = studentService.update(updateDto, id);
        return ResponseEntity.ok(updatedStudent);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        studentService.delete(id);
        return ResponseEntity.noContent().build();
    }
}