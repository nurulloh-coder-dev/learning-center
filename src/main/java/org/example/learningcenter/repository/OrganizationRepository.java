package org.example.learningcenter.repository;

import jakarta.transaction.Transactional;
import org.example.learningcenter.entity.model.Organization;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

@Repository
public interface OrganizationRepository extends JpaRepository<Organization,String> {
    @Query("select o from Organization o where (:search is null or :search ilike o.name) and o.deleted = false")
    Page<Organization> findAll(@Param("search") String search, Pageable pageable);

    @Modifying
    @Transactional
    @Query("UPDATE Organization o set o.deleted = true where o.id=:id")
    void softDelete(@Param("id") String id);
}
