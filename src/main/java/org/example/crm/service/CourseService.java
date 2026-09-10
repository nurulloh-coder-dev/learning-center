package org.example.crm.service;

import jakarta.transaction.Transactional;
import org.example.crm.entity.dto.course.CourseCreateDto;
import org.example.crm.entity.dto.course.CourseDto;
import org.example.crm.entity.dto.course.CourseUpdateDto;
import org.example.crm.entity.model.Course;
import org.example.crm.entity.model.User;
import org.example.crm.exceptions.ErrorCodes;
import org.example.crm.exceptions.ErrorType;
import org.example.crm.exceptions.RestException;
import org.example.crm.filters.CourseFilterDto;
import org.example.crm.mapper.CourseMapper;
import org.example.crm.projection.CourseProjection;
import org.example.crm.repository.CourseRepository;
import org.example.crm.repository.UserRepository;
import org.example.crm.validator.CourseValidator;
import org.example.crm.validator.UserValidator;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class CourseService extends AbstractService<
        CourseRepository,
        CourseMapper,
        CourseValidator> implements CrudService<CourseFilterDto, CourseCreateDto, CourseUpdateDto, CourseDto, String, Page<CourseDto>> {

    private final UserValidator userValidator;
    private final UserRepository userRepository;

    protected CourseService(CourseRepository repository,
                            CourseMapper mapper,
                            CourseValidator validator,
                            UserValidator userValidator,
                            UserRepository userRepository) {
        super(repository, mapper, validator);
        this.userValidator = userValidator;
        this.userRepository = userRepository;
    }

    @Override
    public Page<CourseDto> getAll(Pageable pageable, CourseFilterDto filterDto) {
        String organizationId = userValidator.authenticateAndGetOrganizationId();

        String searchPattern = (filterDto.search() != null && !filterDto.search().isBlank())
                ? "%" + filterDto.search().trim().toLowerCase() + "%"
                : null;

        Page<CourseProjection> courses = repository.getAllByFilter(organizationId, searchPattern, pageable);
        return courses.map(mapper::toDtoFromProjection);
    }

    @Override
    public CourseDto get(String id) {
        Course course = validator.validateIdAndGet(id);
        return mapper.toDto(course);
    }

    @Override
    public CourseDto create(CourseCreateDto createDto) {
        validator.createValid(createDto);
        String userId = userValidator.authenticateAndGetId();
        User currentUser = userRepository.findByIdAndDeletedFalse(userId)
                .orElseThrow(() -> new RestException(ErrorType.USER_NOT_FOUND, ErrorCodes.NotFound));

        Course course = mapper.toEntity(createDto);
        return mapper.toDto(repository.save(course));
    }

    @Override
    public CourseDto update(CourseUpdateDto updateDto, String id) {
        Course course = validator.validateIdAndGet(id);
        mapper.mapUpdate(course, updateDto);
        return mapper.toDto(repository.save(course));
    }

    @Override
    @Transactional
    public void delete(String id) {
        Course course = validator.validateIdAndGet(id);
        repository.updateDeleted(course.getId());
    }
}