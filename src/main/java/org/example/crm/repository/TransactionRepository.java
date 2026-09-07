package org.example.crm.repository;

import jakarta.transaction.Transactional;
import org.example.crm.entity.model.Transaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, String> {

    @Query("""
    SELECT t FROM Transaction t
    LEFT JOIN t.invoice i
    LEFT JOIN t.user u
    LEFT JOIN u.user uu
    WHERE t.deleted = false
      AND t.organizationId = :orgId
      AND (
          CAST(:search AS string) IS NULL
          OR LOWER(i.invoiceNumber) LIKE LOWER(CONCAT('%', CAST(:search AS string), '%'))
          OR LOWER(uu.fullName) LIKE LOWER(CONCAT('%', CAST(:search AS string), '%'))
          OR LOWER(uu.phone) LIKE LOWER(CONCAT('%', CAST(:search AS string), '%'))
          OR CAST(t.amount AS string) LIKE CONCAT('%', CAST(:search AS string), '%')
      )
""")
    Page<Transaction> findAll(
            @Param("search") String search,
            @Param("orgId") String orgId,
            Pageable pageable
    );

    @Transactional
    @Modifying
    @Query("UPDATE Transaction t SET t.deleted = true WHERE t.id = :id and t.organizationId = :organizationId")
    int deleteByIdFalse(@Param("id") String id, @Param("organizationId") String organizationId);

    @Query("select exists (select t.id from Transaction t where t.id=:id)")
    Optional<Boolean> checkId(@Param("id") String id);

    @Query("""
            SELECT COUNT(t.id)
            FROM Transaction t
            WHERE t.organizationId = :organizationId AND t.deleted = false
            """)
    Long countByOrganizationId(@Param("organizationId") String organizationId);
}