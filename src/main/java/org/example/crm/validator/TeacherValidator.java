package org.example.crm.validator;

import org.example.crm.exceptions.ErrorCodes;
import org.example.crm.exceptions.ErrorType;
import org.example.crm.entity.model.Teacher;
import org.example.crm.exceptions.RestException;
import org.example.crm.repository.TeacherRepository;
import org.springframework.stereotype.Component;

@Component
public class TeacherValidator {
    private final TeacherRepository teacherRepository;

    public TeacherValidator(TeacherRepository teacherRepository) {
        this.teacherRepository = teacherRepository;
    }

    public Teacher validateIdAndGet(String id) {
        return teacherRepository.findById(id)
                .orElseThrow(()-> new RestException(ErrorType.TEACHER_NOT_FOUND, ErrorCodes.NotFound));
    }

    public void validate() {

    }
}
