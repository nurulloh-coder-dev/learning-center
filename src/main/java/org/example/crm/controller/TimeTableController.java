package org.example.crm.controller;

import lombok.RequiredArgsConstructor;
import org.example.crm.entity.dto.TimeTableCreateDto;
import org.example.crm.entity.dto.TimeTableUpdateDto;
import org.example.crm.entity.dto.timeTable.TimeTableDto;
import org.example.crm.entity.enums.DayType;
import org.example.crm.service.TimeTableService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalTime;
import java.util.List;

@RestController
@RequestMapping("api/v1/time-table")
@RequiredArgsConstructor
public class TimeTableController {
    final TimeTableService service;


    @GetMapping
    public ResponseEntity<List<TimeTableDto>> findAll(@RequestParam(required = false) DayType dayType,
                                      @RequestParam(required = false, defaultValue = "00:00") LocalTime start,
                                      @RequestParam(required = false, defaultValue = "23:59:59") LocalTime end) {
        return ResponseEntity.ok(service.getAll(dayType,start,end));
    }

    @GetMapping("/{id}")
    public ResponseEntity<TimeTableDto> findById(@PathVariable String id) {
        return ResponseEntity.ok(service.get(id));
    }

    @PostMapping
    public ResponseEntity<TimeTableDto> create(@RequestBody TimeTableCreateDto timeTableDto) {
        return ResponseEntity.ok(service.create(timeTableDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable String id) {
        service.delete(id);
        return ResponseEntity.ok("Time table deleted successfully");
    }

    @PutMapping("/{id}")
    public ResponseEntity<TimeTableDto> update(@PathVariable String id, @RequestBody TimeTableUpdateDto timeTableDto) {
        return ResponseEntity.ok(service.update(timeTableDto, id));
    }


}
