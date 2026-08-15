package org.example.learningcenter.controller;

import jakarta.validation.Valid;
import org.example.learningcenter.entity.dto.teacher.TeacherCreateDto;
import org.example.learningcenter.entity.dto.teacher.TeacherDto;
import org.example.learningcenter.entity.dto.teacher.TeacherUpdateDto;
import org.example.learningcenter.service.TeacherService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("api/v1/teacher")
public class TeacherController {

    private final TeacherService teacherService;

    public TeacherController(TeacherService teacherService) {
        this.teacherService = teacherService;
    }

    @GetMapping
    public ResponseEntity<Page<TeacherDto>> getAll(
            Pageable pageable,
            @RequestParam(required = false) String search
    ) {
        Page<TeacherDto> teachers = teacherService.getAll(pageable, search);
        return ResponseEntity.ok(teachers);
    }

    @GetMapping("/count")
    public ResponseEntity<Map<String, Long>> count() {
        Long count = teacherService.getAllCount();
        return ResponseEntity.ok(Map.of("count",count));
    }

    @GetMapping("/{id}")
    public ResponseEntity<TeacherDto> getById(@PathVariable String id) {
        TeacherDto teacher = teacherService.get(id);
        return ResponseEntity.ok(teacher);
    }

    @PostMapping
    public ResponseEntity<TeacherDto> create(@Valid @RequestBody TeacherCreateDto createDto) {
        TeacherDto createdTeacher = teacherService.create(createDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdTeacher);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TeacherDto> update(
            @PathVariable String id,
            @Valid @RequestBody TeacherUpdateDto updateDto
    ) {
        TeacherDto updatedTeacher = teacherService.update(updateDto, id);
        return ResponseEntity.ok(updatedTeacher);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        teacherService.delete(id);
        return ResponseEntity.noContent().build();
    }
}