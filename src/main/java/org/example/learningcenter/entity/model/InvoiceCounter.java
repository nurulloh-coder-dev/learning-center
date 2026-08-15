package org.example.learningcenter.entity.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Version;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class InvoiceCounter {
    @Id
    private String series;

        @Version
        private Long version; // optimistic locking, optional but safe

        private Long lastNumber;

}
