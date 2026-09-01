package org.example.crm.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.crm.entity.dto.group.FullGroupDto;
import org.example.crm.entity.dto.group.GroupCreateDto;
import org.example.crm.entity.dto.group.GroupDto;
import org.example.crm.entity.dto.group.GroupUpdateDto;
import org.example.crm.entity.enums.GroupStatus;
import org.example.crm.projection.GroupNameProjection;
import org.example.crm.service.GroupService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("api/v1/group")
@RequiredArgsConstructor
public class GroupController {
    final GroupService service;

    @GetMapping
    public ResponseEntity<Page<GroupDto>> getAllGroups(@RequestParam(required = false) String search,
                                                       @RequestParam(defaultValue = "0") int page,
                                                       @RequestParam(defaultValue = "20") int size,
                                                       @RequestParam(required = false) GroupStatus status,
                                                       @RequestParam(required = false) String level) {
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(service.getAll(pageable, search, status, level));
    }

    @GetMapping("/{id}")
    public ResponseEntity<GroupDto> getGroupById(@PathVariable String id) {
        return ResponseEntity.ok(service.get(id));
    }

    @GetMapping("/count")
    public ResponseEntity<Map<String, Integer>> getCount() {
        Integer count = service.getCount();
        return ResponseEntity.ok(Map.of("count", count));
    }

    @PutMapping("/{id}")
    public ResponseEntity<GroupDto> updateGroup(@PathVariable String id, @Valid @RequestBody GroupUpdateDto updateDto) {
        return ResponseEntity.ok(service.update(updateDto, id));
    }

    @PostMapping
    public ResponseEntity<GroupDto> createGroup(@Valid @RequestBody GroupCreateDto groupCreateDto) {
        return ResponseEntity.ok(service.create(groupCreateDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteGroup(@PathVariable String id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/groups")
    public ResponseEntity<List<GroupNameProjection>> getTeacherGroups() {
        List<GroupNameProjection> groupNames = service.getGroupNames();
        return ResponseEntity.ok(groupNames);
    }

    @GetMapping("/groupInfo")
    public ResponseEntity<FullGroupDto> getGroupWithStudents(@RequestParam(required = false) String groupId) {
        FullGroupDto groupInfo = service.getGroupInfo(groupId);
        return ResponseEntity.ok(groupInfo);
    }

    @GetMapping("/group/my")
    public ResponseEntity<List<GroupDto>> getMyGroups(){
        List<GroupDto> myGroups = service.getMyGroups();
        return ResponseEntity.ok(myGroups);
    }
}
