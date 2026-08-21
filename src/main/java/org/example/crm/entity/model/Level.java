package org.example.crm.entity.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;
import org.example.crm.entity.base.BaseEntity;

@Entity
@Setter
@Getter
public class Level extends BaseEntity {
    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private Integer orderNumber;
    @Column(nullable = false)
    private Integer lessonCount;
}
