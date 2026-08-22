package org.example.crm.repository;

import jakarta.transaction.Transactional;
import org.example.crm.entity.enums.LeadStatus;
import org.example.crm.entity.model.Lead;
import org.example.crm.projection.LeadProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface LeadRepository extends JpaRepository<Lead, String> {
    @Query("select exists (select l.id from Lead l where l.id = :id and l.deleted = false)")
    Optional<Boolean> checkId(@Param("id") String id);
    @Query("""
       SELECT 
           l.id AS id,
           l.fullName AS fullName,
           l.phone AS phone,
           l.status AS status,
           l.source AS source,
           l.callAt AS callAt
       FROM Lead l 
       WHERE l.deleted = false 
         AND l.organizationId = :orgId
         AND (CAST(:search AS string) IS NULL OR LOWER(l.fullName) LIKE CAST(:search AS string))
         AND (:status IS NULL OR l.status = :status)
       ORDER BY l.createdAt DESC
       """)
    Page<LeadProjection> findAll(
            @Param("orgId") String organizationId,
            @Param("search") String search,
            @Param("status") LeadStatus status,
            Pageable pageable
    );

    @Modifying
    @Transactional
    @Query("update Lead l set l.deleted=true where l.id=:id")
    Integer softDelete(String id);
}
