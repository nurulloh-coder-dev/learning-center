package org.example.crm.entity.dto.group;

import org.example.crm.entity.dto.student.StudentDto;

import java.util.List;

public record FullGroupDto(List<StudentDto> studentDto, GroupDto groupDto) {

}
