package org.example.crm.controller;

import jakarta.validation.Valid;
import org.example.crm.entity.dto.lesson.LessonCreateDto;
import org.example.crm.entity.dto.lesson.LessonDto;
import org.example.crm.entity.dto.lesson.LessonUpdateDto;
import org.example.crm.service.LessonService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("api/v1/lesson")
public class LessonController {

    private final LessonService lessonService;

    public LessonController(LessonService lessonService) {
        this.lessonService = lessonService;
    }

    @GetMapping
    public ResponseEntity<Page<LessonDto>> getAll(
            Pageable pageable,
            @RequestParam(required = false) String search
    ) {
        Page<LessonDto> lessons = lessonService.getAll(pageable, search);
        return ResponseEntity.ok(lessons);
    }

    @GetMapping("/count")
    public ResponseEntity<Map<String,Long>> count() {
        Long count = lessonService.getAllCount();
        return ResponseEntity.ok(Map.of("count",count));
    }

    @GetMapping("/{id}")
    public ResponseEntity<LessonDto> getById(@PathVariable String id) {
        LessonDto lesson = lessonService.get(id);
        return ResponseEntity.ok(lesson);
    }

    @PostMapping
    public ResponseEntity<LessonDto> create(@Valid @RequestBody LessonCreateDto createDto) {
        LessonDto createdLesson = lessonService.create(createDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdLesson);
    }

    @PutMapping("/{id}")
    public ResponseEntity<LessonDto> update(
            @PathVariable String id,
            @Valid @RequestBody LessonUpdateDto updateDto
    ) {
        LessonDto updatedLesson = lessonService.update(updateDto, id);
        return ResponseEntity.ok(updatedLesson);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        lessonService.delete(id);
        return ResponseEntity.noContent().build();
    }
}