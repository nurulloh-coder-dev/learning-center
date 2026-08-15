package org.example.crm.validator;

import lombok.RequiredArgsConstructor;
import org.example.crm.entity.dto.lesson.LessonCreateDto;
import org.example.crm.exceptions.ErrorCodes;
import org.example.crm.exceptions.ErrorType;
import org.example.crm.entity.model.Lesson;
import org.example.crm.exceptions.RestException;
import org.example.crm.repository.LessonRepository;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class LessonValidator {
    private final LessonRepository repository;

    public Lesson validateIdAndGet(String id) {
        return repository.findById(id)
                .orElseThrow(()-> new RestException(ErrorType.LESSON_NOT_FOUND, ErrorCodes.NotFound));
    }

    public void validate(LessonCreateDto createDto) {

    }
}
