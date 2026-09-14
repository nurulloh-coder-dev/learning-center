package org.example.crm.entity.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.crm.entity.base.BaseEntity;
import org.example.crm.entity.base.TenantEntity;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(
        uniqueConstraints = {
                @UniqueConstraint(
                        columnNames = {"organizationId", "name"}
                )
        }
)
public class Course extends TenantEntity {

    private String name;


}
