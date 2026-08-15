package org.example.learningcenter.validator;

import lombok.RequiredArgsConstructor;
import org.example.learningcenter.entity.dto.TimeTableCreateDto;
import org.example.learningcenter.exceptions.ErrorCodes;
import org.example.learningcenter.exceptions.ErrorType;
import org.example.learningcenter.entity.model.TimeTable;
import org.example.learningcenter.exceptions.RestException;
import org.example.learningcenter.repository.TimeTableRepository;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TimeTableValidator {
    final TimeTableRepository repository;
    public TimeTable validateAndGet(String id) {
        return repository.findById(id).orElseThrow(() -> new RestException(ErrorType.TIMETABLE_NOT_FOUND, ErrorCodes.NotFound));
    }

    public void validate(TimeTableCreateDto createDto) {
        if (createDto.endTime().isBefore(createDto.startTime()) || createDto.endTime().equals(createDto.startTime())) {
            throw new RestException(ErrorType.INVALID_TIME_RANGE, ErrorCodes.BadRequest);
        }
    }
}
