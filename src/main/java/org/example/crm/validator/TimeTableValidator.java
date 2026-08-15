package org.example.crm.validator;

import lombok.RequiredArgsConstructor;
import org.example.crm.entity.dto.TimeTableCreateDto;
import org.example.crm.exceptions.ErrorCodes;
import org.example.crm.exceptions.ErrorType;
import org.example.crm.entity.model.TimeTable;
import org.example.crm.exceptions.RestException;
import org.example.crm.repository.TimeTableRepository;
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
