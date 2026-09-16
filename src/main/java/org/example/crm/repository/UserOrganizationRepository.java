package org.example.crm.repository;

import org.example.crm.entity.enums.Role;
import org.example.crm.entity.model.UserOrganization;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserOrganizationRepository extends JpaRepository<UserOrganization, String> {

    @EntityGraph(attributePaths = {"user", "organization"})
    List<UserOrganization> findAllByUserIdAndDeletedFalse(String userId);

    @Query("select uo from UserOrganization uo where uo.organization.id=:orgId and uo.user.id=:userId and uo.deleted=false")
    Optional<UserOrganization> findByUserIdAndOrgId(@Param("userId") String id, @Param("orgId") String organizationId);

    @Query("""
                    select uo from UserOrganization uo
                    where uo.user.id = :userId
                    and uo.organization.id = :organizationId
                    and uo.deleted = false
                    and uo.organization.deleted = false
            """)
    Optional<UserOrganization> findUserOrganizationByUserIdAndOrganizationId(String userId, String organizationId);

    @Query("select uo from UserOrganization uo where uo.user.id=:userId and uo.deleted=false")
    Optional<UserOrganization> findUserOrganizationByUserId(String userId);

    @EntityGraph(attributePaths = {"user", "branch"})
    @Query("""
                SELECT uo FROM UserOrganization uo
                JOIN uo.user u
                WHERE uo.deleted = false
                  AND uo.organization.id = :organizationId
                  AND (
                      CAST(:search AS string) IS NULL 
                      OR LOWER(u.fullName) LIKE LOWER(CONCAT('%', CAST(:search AS string), '%'))
                      OR u.phone LIKE CONCAT('%', :search, '%')
                  )
            """)
    Page<UserOrganization> findAllByOrganizationIdAndFilter(
            @Param("organizationId") String organizationId,
            @Param("search") String search,
            Pageable pageable
    );

    @Query("select exists (select uo.id from UserOrganization uo where uo.user.id=:userId and uo.organization.id=:orgId and uo.deleted=false and uo.role=:role)")
    boolean checkIfAlreadyInOrganization(@Param("orgId") String organizationId, @Param("orgId") String id,@Param("role") Role role);
}
