package org.example.crm.entity.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;
import org.example.crm.entity.enums.AdministratorPermission;
import org.example.crm.entity.enums.Role;

import java.time.LocalDate;
import java.util.List;

@Schema(description = "Permissions assigned to the administrator")
public record UserCreateDto(String fullName, String phone, LocalDate birthDate, Role role, String branchId,
                            @Schema(
                                    description = "List of permissions",
                                    allowableValues = {
                                            "LEAD_MANAGEMENT",
                                            "TEACHER_MANAGEMENT",
                                            "STUDENT_MANAGEMENT",
                                            "INVOICE_MANAGEMENT"
                                    }
                            )
                            List<AdministratorPermission> permissions) {
}
