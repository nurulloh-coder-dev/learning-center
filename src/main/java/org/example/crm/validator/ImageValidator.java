package org.example.crm.validator;

import lombok.RequiredArgsConstructor;
import org.example.crm.exceptions.ErrorCodes;
import org.example.crm.exceptions.ErrorType;
import org.example.crm.exceptions.RestException;
import org.example.crm.repository.ImageRepository;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ImageValidator {
    private final ImageRepository repository;

    public void validateId(String id) {
        Boolean exists = repository.checkId(id).orElse(false);
        if (!exists){
            throw new RestException(ErrorType.ATTACHMENT_NOT_FOUND, ErrorCodes.NotFound);
        }
    }
}
