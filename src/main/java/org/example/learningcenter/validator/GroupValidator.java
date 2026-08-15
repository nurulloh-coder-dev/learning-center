package org.example.learningcenter.validator;

import lombok.RequiredArgsConstructor;
import org.example.learningcenter.entity.dto.group.GroupCreateDto;
import org.example.learningcenter.exceptions.ErrorCodes;
import org.example.learningcenter.exceptions.ErrorType;
import org.example.learningcenter.entity.model.Group;
import org.example.learningcenter.exceptions.RestException;
import org.example.learningcenter.repository.GroupRepository;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class GroupValidator {
    final GroupRepository repository;
    public void createValid(GroupCreateDto createDto) {
        if (repository.existsGroupByName(createDto.name())) {
            throw new RestException(ErrorType.GROUP_ALREADY_EXISTS_WITH_THIS_NAME, ErrorCodes.AlreadyExists);
        }

    }

    public Group validateIdAndGet(String id) {
        return repository.findById(id).orElseThrow(() ->
                new RestException(ErrorType.GROUP_NOT_FOUND, ErrorCodes.NotFound));
    }
}
