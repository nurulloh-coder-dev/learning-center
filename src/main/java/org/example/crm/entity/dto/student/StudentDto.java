package org.example.crm.entity.dto.student;

import io.swagger.v3.oas.annotations.media.Schema;
import org.example.crm.entity.dto.user.UserDto;
import org.example.crm.entity.enums.EnrollmentPaymentStatus;

import java.math.BigDecimal;

public record StudentDto(String id,
                         UserDto userDto,
                         String parentPhone,
                         BigDecimal balance
                         ) {
}
