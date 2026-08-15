package org.example.learningcenter.entity.dto.group;

import org.example.learningcenter.entity.dto.student.StudentDto;

import java.util.List;

public record FullGroupDto(List<StudentDto> studentDto, GroupDto groupDto) {

}
