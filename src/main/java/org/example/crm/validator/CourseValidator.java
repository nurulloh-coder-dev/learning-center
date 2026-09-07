package org.example.crm.validator;

import lombok.RequiredArgsConstructor;
import org.example.crm.entity.dto.course.CourseCreateDto;
import org.example.crm.entity.model.Course;
import org.example.crm.exceptions.ErrorCodes;
import org.example.crm.exceptions.ErrorType;
import org.example.crm.exceptions.RestException;
import org.example.crm.repository.CourseRepository;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CourseValidator {

    private final CourseRepository repository;
    private final UserValidator userValidator;

    public Course validateIdAndGet(String id) {
        if (id == null || id.isBlank()) {
            throw new RestException(ErrorType.INVALID_INPUT, ErrorCodes.BadRequest);
        }
        return repository.findByIdAndDeletedFalse(id)
                .orElseThrow(() -> new RestException(ErrorType.COURSE_NOT_FOUND, ErrorCodes.NotFound));
    }

    public void createValid(CourseCreateDto createDto) {
        String organizationId = userValidator.authenticateAndGetOrganizationId();
        if (repository.existsByNameAndOrganizationIdAndDeletedFalse(createDto.name(), organizationId)) {
            throw new RestException(ErrorType.COURSE_ALREADY_EXISTS, ErrorCodes.BadRequest);
        }
    }
}