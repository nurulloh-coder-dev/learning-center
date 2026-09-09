package org.example.crm.validator;

import lombok.AllArgsConstructor;
import org.example.crm.exceptions.ErrorCodes;
import org.example.crm.exceptions.ErrorType;
import org.example.crm.entity.model.Enrollment;
import org.example.crm.exceptions.RestException;
import org.example.crm.repository.EnrollmentRepository;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class EnrollmentValidator {
    private final EnrollmentRepository repository;
    private final UserValidator userValidator;

    public void validateId(String id) {
        Boolean exists = repository.checkId(id).orElse(false);
        if (!exists) {
            throw new RestException(ErrorType.ENROLLMENT_NOT_FOUND, ErrorCodes.NotFound);
        }
    }

    public Enrollment validateIdAndGet(String id) {
        Enrollment enrollment = repository.findById(id)
                .orElseThrow(() -> new RestException(ErrorType.ENROLLMENT_NOT_FOUND, ErrorCodes.NotFound));
        String organizationId = userValidator.authenticateAndGetOrganizationId();
        if (!enrollment.getOrganizationId().equals(organizationId)){
            throw new RestException(ErrorType.FORBIDDEN,ErrorCodes.Forbidden);
        }
        return enrollment;
    }
}
