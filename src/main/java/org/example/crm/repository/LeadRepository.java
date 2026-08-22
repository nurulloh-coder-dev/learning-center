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

public interface LeadRepository extends JpaRepository<Lead,String> {
    @Query("""
           select l.id,
                  l.fullName,
                  l.phone,
                  l.status,
                  l.source,
                  l.callAt
                  from Lead l where l.deleted = false and l.organizationId=:orgId
                  and (:search is null or l.fullName ilike :search)
                  and (:status is null or l.status =:status)
                  order by l.createdAt desc""")
    Page<LeadProjection> findAll(@Param("orgId") String organizationId, @Param("search") String search,@Param("status") LeadStatus status, Pageable pageable);

    @Query("select exists (select l.id from Lead l where l.id = :id and l.deleted = false)")
    Optional<Boolean> checkId(@Param("id") String id);

    @Modifying
    @Transactional
    @Query("update Lead l set l.deleted=true where l.id=:id")
    Integer softDelete(String id);
}
