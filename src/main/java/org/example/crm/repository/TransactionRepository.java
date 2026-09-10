package org.example.crm.repository;

import jakarta.transaction.Transactional;
import org.example.crm.entity.enums.TransactionType;
import org.example.crm.entity.model.Transaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, String>, JpaSpecificationExecutor<Transaction> {
    @Query("select exists (select t.id from Transaction t where t.id=:id)")
    Optional<Boolean> checkId(@Param("id") String id);

    @Query("""
            SELECT COUNT(t.id)
            FROM Transaction t
            WHERE t.organizationId = :organizationId AND t.deleted = false
            """)
    Long countByOrganizationId(@Param("organizationId") String organizationId);
}