package org.example.learningcenter.repository;

import jakarta.persistence.LockModeType;
import org.example.learningcenter.entity.model.InvoiceCounter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface InvoiceCounterRepository extends JpaRepository<InvoiceCounter, String> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("""

 select c from InvoiceCounter c where c.series = :id
 """)
    Optional<InvoiceCounter> findByIdForUpdate(String id);
}
