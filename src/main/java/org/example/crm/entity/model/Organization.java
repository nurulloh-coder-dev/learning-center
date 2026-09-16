package org.example.crm.entity.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.PrePersist;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.crm.entity.base.BaseEntity;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Organization extends BaseEntity {
    private String name;
    private String phone;
    private String email;
    private String website;
    private Integer daysBeforeDebt;



    @PrePersist
    public void setDaysBeforeDebtOnPersist() {
        if (daysBeforeDebt == null) {
            daysBeforeDebt = 3; // Set a default value
        }
    }
}
