package org.example.crm.validator;

import lombok.RequiredArgsConstructor;
import org.example.crm.entity.dto.group.GroupCreateDto;
import org.example.crm.exceptions.ErrorCodes;
import org.example.crm.exceptions.ErrorType;
import org.example.crm.entity.model.Group;
import org.example.crm.exceptions.RestException;
import org.example.crm.repository.GroupRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;

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


    public Group validateIdOrgAndGet(String id, String organizationId) {
        return repository.findByIdAndOrganizationId(id, organizationId).orElseThrow(() ->
                new RestException(ErrorType.GROUP_NOT_FOUND_OR_NOT_RELATED_TO_ORGANIZATION, ErrorCodes.NotFound));
    }

    public String validateIdAndGetBranchId(String groupId) {
        Optional<String> branchId = repository.checkAndGetBranchId(groupId);
        if (branchId.isEmpty()){
            throw new RestException(ErrorType.BRANCH_NOT_FOUND,ErrorCodes.NotFound);
        }
        return branchId.get();
    }

    public Integer validateIdAndGetMonth(String groupId, Integer previousMonths) {
        Optional<Integer> month = repository.checkAndGetCurrentMonth(groupId);
        if (month.isEmpty()){
            throw new RestException(ErrorType.GROUP_NOT_FOUND,ErrorCodes.NotFound);
        }
        Integer currentMonth = month.get();
        if (currentMonth-previousMonths<=0){
            throw new RestException(ErrorType.BAD_REQUEST,ErrorCodes.BadRequest);
        }
        return currentMonth-previousMonths;
    }
}
