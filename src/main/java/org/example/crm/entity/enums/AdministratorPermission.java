package org.example.crm.entity.enums;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(enumAsRef = true)
public enum AdministratorPermission {
    LEAD_MANAGEMENT,
    EMPLOYEE_MANAGEMENT,
    TEACHER_MANAGEMENT,
    STUDENT_MANAGEMENT,
    INVOICE_MANAGEMENT
}
