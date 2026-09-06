package org.example.crm.repository;

import org.example.crm.entity.model.Course;
import org.example.crm.projection.CourseProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CourseRepository extends JpaRepository<Course, String> {

    @Query("""
        SELECT c.id AS id,
               c.name AS name
        FROM Course c
        WHERE c.deleted = false
          AND c.organizationId = :orgId
          AND (
              CAST(:search AS string) IS NULL
              OR LOWER(c.name) LIKE LOWER(:search)
          )
    """)
    Page<CourseProjection> getAllByFilter(
            @Param("orgId") String orgId,
            @Param("search") String search,
            Pageable pageable
    );

    @Query("SELECT c FROM Course c WHERE c.id = :id AND c.deleted = false")
    Optional<Course> findByIdAndDeletedFalse(@Param("id") String id);

    @Modifying
    @Query("UPDATE Course c SET c.deleted = true WHERE c.id = :id")
    void updateDeleted(@Param("id") String id);

    @Query("SELECT COUNT(c.id) FROM Course c WHERE c.deleted = false AND c.organizationId = :orgId")
    Optional<Integer> getCount(@Param("orgId") String orgId);

    @Query("""
        SELECT CASE WHEN COUNT(c) > 0 THEN true ELSE false END 
        FROM Course c 
        WHERE c.name = :name 
          AND c.organizationId = :orgId 
          AND c.deleted = false
    """)
    boolean existsByNameAndOrganizationIdAndDeletedFalse(@Param("name") String name, @Param("orgId") String orgId);
}