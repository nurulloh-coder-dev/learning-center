package org.example.crm.controller;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.crm.entity.dto.groupLevel.GroupLevelCreateDto;
import org.example.crm.entity.dto.groupLevel.GroupLevelDto;
import org.example.crm.entity.dto.groupLevel.GroupLevelNameDto;
import org.example.crm.entity.request.GroupLevelList;
import org.example.crm.service.GroupLevelService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/group-level")
public class GroupLevelController {

    final GroupLevelService service;


    @GetMapping
    public ResponseEntity<List<GroupLevelDto>> getGroupLevels(@RequestParam(required = false) String search) {
        return ResponseEntity.ok(service.getGroupLevels(search));
    }

    @GetMapping("/names")
    public ResponseEntity<List<GroupLevelNameDto>> getGroupLevelsName() {
        return ResponseEntity.ok(service.getGroupLevelsName());
    }

    @GetMapping("/{id}")
    public ResponseEntity<GroupLevelDto> getById(@PathVariable String id) {
        GroupLevelDto lesson = service.get(id);
        return ResponseEntity.ok(lesson);
    }

    @PostMapping
    public ResponseEntity<GroupLevelDto> create(@Valid @RequestBody GroupLevelCreateDto createDto) {
        GroupLevelDto createdGroupLevel = service.create(createDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdGroupLevel);
    }

    @PutMapping()
    public ResponseEntity<List<GroupLevelDto>> update(
            @Valid @RequestBody GroupLevelList updateDto
    ) {
        List<GroupLevelDto> updatedGroupLevel = service.update(updateDto.levels());
        return ResponseEntity.ok(updatedGroupLevel);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
