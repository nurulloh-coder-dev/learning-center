package org.example.crm.entity.model;

import jakarta.persistence.*;
import lombok.*;
import org.example.crm.entity.base.BaseEntity;
import org.example.crm.entity.enums.FeatureKey;

import java.math.BigDecimal;
import java.util.EnumMap;
import java.util.Map;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Plan extends BaseEntity {

    @Column(unique = true, nullable = false)
    private String code;                  // START, STANDARD, PRO

    @Column(nullable = false)
    private String name;

    private String description;

    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal price;

    @Column(nullable = false)
    private String currency = "UZS";

    @Column(nullable = false)
    private Integer durationMonths = 1;   // 1, 3, 6, 12

    @Column(nullable = false)
    private Boolean active = true;

    private Integer sortOrder;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(
            name = "plan_limit",
            joinColumns = @JoinColumn(name = "plan_id"),
            uniqueConstraints = @UniqueConstraint(columnNames = {"plan_id", "feature_key"})
    )
    @MapKeyEnumerated(EnumType.STRING)
    @MapKeyColumn(name = "feature_key")
    @Column(name = "limit_value", nullable = false)
    private Map<FeatureKey, Integer> limits = new EnumMap<>(FeatureKey.class);
}