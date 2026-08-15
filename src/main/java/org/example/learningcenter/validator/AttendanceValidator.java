package org.example.learningcenter.validator;

import lombok.RequiredArgsConstructor;
import org.example.learningcenter.exceptions.ErrorCodes;
import org.example.learningcenter.exceptions.ErrorType;
import org.example.learningcenter.entity.model.Attendance;
import org.example.learningcenter.exceptions.RestException;
import org.example.learningcenter.repository.AttendanceRepository;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AttendanceValidator {
    private final AttendanceRepository repository;

    public Attendance validateIdAndGet(String id) {
       return repository.findById(id)
                .orElseThrow(()->new RestException(ErrorType.ATTENDANCE_NOT_FOUND, ErrorCodes.NotFound));

    }

    public void validateId(String id) {
        Boolean exists = repository.checkId(id).orElse(false);
        if (!exists){
            throw new RestException(ErrorType.ATTENDANCE_NOT_FOUND, ErrorCodes.NotFound);
        }
    }

}
