package org.example.crm.validator;

import lombok.RequiredArgsConstructor;
import org.example.crm.entity.dto.lead.LeadCreateDto;
import org.example.crm.entity.dto.lead.LeadUpdateDto;
import org.example.crm.entity.model.Lead;
import org.example.crm.exceptions.ErrorCodes;
import org.example.crm.exceptions.ErrorType;
import org.example.crm.exceptions.RestException;
import org.example.crm.repository.LeadRepository;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class LeadValidator {
    private final LeadRepository repository;

    public Lead validateIdAndGet(String id) {
       return repository.findById(id)
                .orElseThrow(()->new RestException(ErrorType.LEAD_NOT_FOUND, ErrorCodes.NotFound));
    }

    public void validate(LeadCreateDto createDto) {


    }

    public void validate(LeadUpdateDto updateDto) {

    }

    public void validateId(String id) {
        Boolean exists = repository.checkId(id).orElse(false);
        if (!exists){
            throw new RestException(ErrorType.LEAD_NOT_FOUND,ErrorCodes.NotFound);
        }

    }
}
