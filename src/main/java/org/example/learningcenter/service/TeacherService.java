package org.example.learningcenter.service;

import org.example.learningcenter.entity.dto.teacher.TeacherCreateDto;
import org.example.learningcenter.entity.dto.teacher.TeacherDto;
import org.example.learningcenter.entity.dto.teacher.TeacherUpdateDto;
import org.example.learningcenter.entity.model.Teacher;
import org.example.learningcenter.mapper.TeacherMapper;
import org.example.learningcenter.repository.TeacherRepository;
import org.example.learningcenter.validator.TeacherValidator;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class TeacherService extends AbstractService<
        TeacherRepository,
        TeacherMapper,
        TeacherValidator> implements CrudService<TeacherCreateDto, TeacherUpdateDto, TeacherDto,String>{

    protected TeacherService(TeacherRepository repository, TeacherMapper mapper, TeacherValidator validator) {
        super(repository, mapper, validator);
    }

    @Override
    public Page<TeacherDto> getAll(Pageable pageable, String search) {
        Page<Teacher> all = repository.findAll(pageable, search);
        return all.map(mapper::toDto);
    }

    @Override
    public TeacherDto get(String id) {
        Teacher teacher = validator.validateIdAndGet(id);
        return mapper.toDto(teacher);
    }

    @Override
    public TeacherDto create(TeacherCreateDto createDto) {
       validator.validate();
        Teacher entity = mapper.toEntity(createDto);
        return mapper.toDto(repository.save(entity));
    }

    @Override
    public TeacherDto update(TeacherUpdateDto updateDto, String id) {
        Teacher teacher = validator.validateIdAndGet(id);
        mapper.mapUpdate(teacher,updateDto);
        return mapper.toDto(repository.save(teacher));
    }

    @Override
    public void delete(String id) {
        Teacher teacher = validator.validateIdAndGet(id);
        teacher.setDeleted(true);
        repository.save(teacher);
    }

    public Long getAllCount() {
        return repository.countTeachersByDeleted(false);
    }
}
