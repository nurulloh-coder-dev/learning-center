package org.example.learningcenter.validator;

import lombok.RequiredArgsConstructor;
import org.example.learningcenter.entity.model.Branch;
import org.example.learningcenter.exceptions.ErrorCodes;
import org.example.learningcenter.exceptions.ErrorType;
import org.example.learningcenter.exceptions.RestException;
import org.example.learningcenter.repository.BranchRepository;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BranchValidator {
    final BranchRepository branchRepository;

    public void validate(String name) {
        boolean b = branchRepository.existsBranchByName(name);
        if (b) {
            throw new RestException(ErrorType.BRANCH_ALREADY_EXISTS, ErrorCodes.AlreadyExists);
        }
    }

    public Branch validateIdAndGet(String id) {
        return branchRepository.findById(id).orElseThrow(() ->
                new RestException(ErrorType.BRANCH_NOT_FOUND, ErrorCodes.NotFound));
    }

    public void validateId(String id) {
        Boolean exists = branchRepository.checkId(id).orElse(false);
        if (!exists){
            throw new RestException(ErrorType.BRANCH_NOT_FOUND,ErrorCodes.NotFound);
        }
    }
}
