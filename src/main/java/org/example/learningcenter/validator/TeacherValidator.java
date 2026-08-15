package org.example.learningcenter.validator;

import org.example.learningcenter.exceptions.ErrorCodes;
import org.example.learningcenter.exceptions.ErrorType;
import org.example.learningcenter.entity.model.Teacher;
import org.example.learningcenter.exceptions.RestException;
import org.example.learningcenter.repository.TeacherRepository;
import org.springframework.http.HttpStatus;
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
