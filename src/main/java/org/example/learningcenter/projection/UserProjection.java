package org.example.learningcenter.projection;

import java.time.LocalDate;

public interface UserProjection {
    String getId();
    String getFullName();
    String getPhone();
    LocalDate getBirthDate();
}