package org.example.crm.validator;

import lombok.AllArgsConstructor;
import org.example.crm.entity.dto.student.StudentCreateDto;
import org.example.crm.exceptions.ErrorCodes;
import org.example.crm.exceptions.ErrorType;
import org.example.crm.entity.model.Student;
import org.example.crm.exceptions.RestException;
import org.example.crm.repository.StudentRepository;
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

    public Student validateStudentByUserId(String id) {
        return repository.findByUserId(id)
                .orElseThrow(()-> new RestException(ErrorType.STUDENT_NOT_FOUND, ErrorCodes.NotFound));
    }
}
