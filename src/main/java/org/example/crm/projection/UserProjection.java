package org.example.crm.projection;

import java.time.LocalDate;

public interface UserProjection {
    String getId();
    String getFullName();
    String getPhone();
    LocalDate getBirthDate();
}