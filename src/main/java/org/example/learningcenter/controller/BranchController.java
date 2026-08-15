package org.example.learningcenter.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.learningcenter.annotation.CurrentUser;
import org.example.learningcenter.entity.dto.branch.BranchCreateDto;
import org.example.learningcenter.entity.dto.branch.BranchDto;
import org.example.learningcenter.entity.dto.branch.BranchUpdateDto;
import org.example.learningcenter.entity.model.User;
import org.example.learningcenter.service.BranchService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/branch")
public class BranchController {

    private final BranchService branchService;

    @GetMapping
    public ResponseEntity<Page<BranchDto>> getAll(
            Pageable pageable,
            @RequestParam(required = false) String search
    ) {
        Page<BranchDto> lessons = branchService.getAll(pageable, search);
        return ResponseEntity.ok(lessons);
    }

    @GetMapping("/count")
    public ResponseEntity<Map<String,Long>> count(@CurrentUser User user) {
        Long count = branchService.getAllCount(user);
        return ResponseEntity.ok(Map.of("count",count));
    }

    @GetMapping("/{id}")
    public ResponseEntity<BranchDto> getById(@PathVariable String id) {
        BranchDto lesson = branchService.get(id);
        return ResponseEntity.ok(lesson);
    }

    @PostMapping
    public ResponseEntity<BranchDto> create(@Valid @RequestBody BranchCreateDto createDto) {
        BranchDto createdBranch = branchService.create(createDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdBranch);
    }

    @PutMapping("/{id}")
    public ResponseEntity<BranchDto> update(
            @PathVariable String id,
            @Valid @RequestBody BranchUpdateDto updateDto
    ) {
        BranchDto updatedBranch = branchService.update(updateDto, id);
        return ResponseEntity.ok(updatedBranch);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        branchService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
