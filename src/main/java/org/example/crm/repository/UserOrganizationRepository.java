package org.example.crm.repository;

import org.example.crm.entity.model.UserOrganization;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserOrganizationRepository extends JpaRepository<UserOrganization, String> {

    @EntityGraph(attributePaths = {"user", "organization"})
    List<UserOrganization> findAllByUserIdAndDeletedFalse(String userId);

    List<UserOrganization> findAllByUserId(String userId);

    @Query("select uo from UserOrganization uo where uo.organization.id=:orgId and uo.user.id=:userId and uo.deleted=false")
    Optional<UserOrganization> findByUserIdAndOrgId(@Param("userId") String id,@Param("orgId") String organizationId);

    @Query("""
        select uo from UserOrganization uo
        where uo.user.id = :userId
        and uo.organization.id = :organizationId
        and uo.deleted = false
        and uo.organization.deleted = false
        and uo.user.deleted = false
""")
    Optional<UserOrganization> findUserOrganizationByUserIdAndOrganization_Id(String userId, String organizationId);
}
