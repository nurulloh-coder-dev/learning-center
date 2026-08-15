package org.example.crm.validator;

import lombok.RequiredArgsConstructor;
import org.example.crm.entity.dto.group.GroupCreateDto;
import org.example.crm.exceptions.ErrorCodes;
import org.example.crm.exceptions.ErrorType;
import org.example.crm.entity.model.Group;
import org.example.crm.exceptions.RestException;
import org.example.crm.repository.GroupRepository;
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
