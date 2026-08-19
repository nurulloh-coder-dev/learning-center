package org.example.crm.entity.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.crm.entity.base.BaseEntity;

import java.math.BigDecimal;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Branch  extends BaseEntity {
    private BigDecimal chargeForMonth;

    @Column(nullable = false)
    private String name;

    private String address;

    // Google Maps Data
    private String googlePlaceId;
    private Double latitude;
    private Double longitude;
    private String googleMapsUrl;
}
