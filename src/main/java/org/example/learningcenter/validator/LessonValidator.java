package org.example.learningcenter.validator;

import lombok.RequiredArgsConstructor;
import org.example.learningcenter.entity.dto.lesson.LessonCreateDto;
import org.example.learningcenter.exceptions.ErrorCodes;
import org.example.learningcenter.exceptions.ErrorType;
import org.example.learningcenter.entity.model.Lesson;
import org.example.learningcenter.exceptions.RestException;
import org.example.learningcenter.repository.LessonRepository;
import org.springframework.http.HttpStatus;
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
