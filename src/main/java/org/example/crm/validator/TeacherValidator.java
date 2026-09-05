package org.example.crm.validator;

import org.example.crm.entity.dto.teacher.TeacherCreateDto;
import org.example.crm.exceptions.ErrorCodes;
import org.example.crm.exceptions.ErrorType;
import org.example.crm.entity.model.Teacher;
import org.example.crm.exceptions.RestException;
import org.example.crm.repository.TeacherRepository;
import org.springframework.stereotype.Component;

@Component
public class TeacherValidator {
    private final TeacherRepository repository;

    public TeacherValidator(TeacherRepository teacherRepository) {
        this.repository = teacherRepository;
    }

    public Teacher validateIdAndGet(String id) {
        return repository.findById(id)
                .orElseThrow(() -> new RestException(ErrorType.TEACHER_NOT_FOUND, ErrorCodes.NotFound));
    }

    public Teacher validateIdAndGetOrg(String id, String organizationId) {
        return repository.findTeacherByIdAndOrg(id, organizationId)
                .orElseThrow(() -> new RestException(ErrorType.TEACHER_NOT_FOUND, ErrorCodes.NotFound));
    }

    public void validateId(String id) {
        Boolean exists = repository.checkId(id).orElse(false);
        if (!exists) {
            throw new RestException(ErrorType.TEACHER_NOT_FOUND, ErrorCodes.NotFound);
        }
    }

    public void validateId(String id, String organizationId) {
        Boolean exists = repository.checkIdAndOrgId(id,organizationId).orElse(false);
        if (!exists) {
            throw new RestException(ErrorType.TEACHER_NOT_FOUND, ErrorCodes.NotFound);
        }
    }

    public void validate(TeacherCreateDto createDto) {

    }

    public void validateGroupAndTeacher(String userId, String groupId) {
        boolean validated = repository.validateGroupAndTeacher(userId, groupId);
        if (!validated){
            throw new RestException(ErrorType.FORBIDDEN,ErrorCodes.Forbidden);
        }
    }
}
