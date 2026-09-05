package org.example.crm.repository;

import jakarta.transaction.Transactional;
import org.example.crm.entity.model.Branch;
import org.example.crm.projection.AnalyticBranchProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BranchRepository extends JpaRepository<Branch, String> {

    @Query("""
    SELECT b FROM Branch b
    WHERE b.deleted = false
      AND b.organizationId = :orgId
      AND (
          CAST(:search AS string) IS NULL
          OR LOWER(b.name) LIKE LOWER(CONCAT('%', CAST(:search AS string), '%'))
          OR LOWER(b.address) LIKE LOWER(CONCAT('%', CAST(:search AS string), '%'))
      )
""")
    Page<Branch> findAll(
            @Param("search") String search,
            @Param("orgId") String orgId,
            Pageable pageable
    );

    boolean existsBranchByName(String name);

    @Transactional
    @Modifying
    @Query("UPDATE Branch b SET b.deleted = true WHERE b.id = :id and b.organizationId =:organizationId")
    int deleteByIdFalse(@Param("id") String id, @Param("organizationId") String organizationId);

    @Query("select exists (select b.id from Branch b where b.id=:id)")
    Optional<Boolean> checkId(@Param("id") String id);

    @Query("""
        select distinct count(b.id)
        from Branch b
        where b.organizationId = :organizationId
        and b.deleted = false
        
""")
    AnalyticBranchProjection getAnalyticBranch(String organizationId);
}
