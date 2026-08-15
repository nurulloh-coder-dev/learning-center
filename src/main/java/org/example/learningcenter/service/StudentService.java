package org.example.learningcenter.service;

import org.example.learningcenter.entity.dto.student.StudentDto;
import org.example.learningcenter.entity.dto.student.StudentUpdateDto;
import org.example.learningcenter.entity.dto.student.StudentCreateDto;
import org.example.learningcenter.entity.model.Student;
import org.example.learningcenter.mapper.StudentMapper;
import org.example.learningcenter.projection.StudentProjection;
import org.example.learningcenter.projection.StudentShowProjection;
import org.example.learningcenter.repository.GroupRepository;
import org.example.learningcenter.repository.StudentRepository;
import org.example.learningcenter.validator.StudentValidator;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StudentService extends AbstractService<
        StudentRepository,
        StudentMapper,
        StudentValidator> implements CrudService<StudentCreateDto, StudentUpdateDto, StudentDto, String> {

    final UserService userService;
    protected StudentService(StudentRepository repository, StudentMapper mapper, StudentValidator validator, UserService userService) {
        super(repository, mapper, validator);
        this.userService = userService;
    }

    @Override
    public Page<StudentDto> getAll(Pageable pageable, String search) {
        Page<StudentProjection> all = repository.searchStudents(search, pageable);
        return all.map(mapper::toDtoProj);
    }

    @Override
    public StudentDto get(String id) {
        Student student = validator.validateIdAndGet(id);
        return mapper.toDto(student);
    }

    @Override
    public StudentDto create(StudentCreateDto createDto) {
        validator.validate(createDto);
        Student entity = mapper.toEntity(createDto);
        return mapper.toDto(repository.save(entity));
    }

    @Override
    public StudentDto update(StudentUpdateDto updateDto, String id) {
        Student student = validator.validateIdAndGet(id);
        mapper.mapUpdate(student, updateDto);
        return mapper.toDto(repository.save(student));
    }

    @Override
    public void delete(String id) {
        Student student = validator.validateIdAndGet(id);
        student.setDeleted(true);
        repository.save(student);
    }

    public Long getAllCount() {
        return repository.countStudentsByDeleted(false);
    }

    public List<StudentDto> getStudentsByGroupId(String groupId) {
        List<StudentShowProjection> studentByGroupId = repository.getStudentShowByGroupId(groupId);
        return studentByGroupId
                .stream()
                .map(mapper::toDtoShowProj)
                .toList();
    }

    public List<StudentDto> getByPhone(String phone) {
        List<Student> studentsByPhone = repository.getStudentByPhone(phone);
        return studentsByPhone
                .stream()
                .map(mapper::toDto)
                .toList();
    }
}
