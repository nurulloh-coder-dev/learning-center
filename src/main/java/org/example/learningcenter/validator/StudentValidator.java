package org.example.learningcenter.validator;

import lombok.AllArgsConstructor;
import org.example.learningcenter.entity.dto.student.StudentCreateDto;
import org.example.learningcenter.exceptions.ErrorCodes;
import org.example.learningcenter.exceptions.ErrorType;
import org.example.learningcenter.entity.model.Student;
import org.example.learningcenter.exceptions.RestException;
import org.example.learningcenter.repository.StudentRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class StudentValidator {
    private final StudentRepository repository;

    public Student validateIdAndGet(String id) {
        return repository.findById(id)
                .orElseThrow(()-> new RestException(ErrorType.STUDENT_NOT_FOUND, ErrorCodes.NotFound));
    }

    public void validateId(String id) {
        Boolean exists = repository.checkId(id).orElse(false);
        if (!exists){
            throw new RestException(ErrorType.STUDENT_NOT_FOUND, ErrorCodes.NotFound);
        }
    }

    public void validate(StudentCreateDto createDto) {

    }
}
