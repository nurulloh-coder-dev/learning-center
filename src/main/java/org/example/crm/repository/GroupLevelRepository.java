package org.example.crm.repository;

import org.example.crm.entity.model.Level;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface GroupLevelRepository extends JpaRepository<Level, String> {

    @Query("""
            select l
            from Level l
            where l.organizationId = :organizationId
            and (:search is null or l.name ilike concat('%', cast(:search as string ) , '%'))
                        order by l.orderNumber asc
            """)
    List<Level> findAllLevelsByOrganizationIdAndSearch(String organizationId, String search);

    @Query("""
        select l
        from Level l
        where l.id = :id
        and l.organizationId = :organizationId
        """)
    Optional<Level> findLevelByIdAndOrganizationId(String id, String organizationId);


    @Query("""
        select case when count(l) > 0 then true else false end
        from Level l
        where l.id = :id
        and l.orderNumber in (
            select l2.orderNumber
            from Level l2
            where l2.organizationId = l.organizationId
            and l2.deleted = false
        )
        and l.deleted = false
""")
    boolean checkLevelOrder(String id);

    @Query("""
        select max(l.orderNumber)
        from Level l
        where l.organizationId = :organizationId
        and l.deleted = false
""")
    Integer getMaxOrderNumberByOrganizationId(String organizationId);

    @Query("""
        select case when count(l) > 0 then true else false end
        from Level l
        where l.organizationId = :organizationId
        and lower(l.name) = lower(:name)
        and l.deleted = false
""")
    boolean existsByOrganizationIdAndNameIgnoreCase(String organizationId, String name);


    @Modifying
    @Query("UPDATE Level l SET l.deleted = true WHERE l.id = :id")
    void updateLevelDeleted(String id);

    @Query("""
       select l
       from Level l
         where l.organizationId = :organizationId and
         l.orderNumber > :orderNumber and
         l.deleted = false
         order by l.orderNumber asc
         
""")
    Optional<Level> getNextLevelForGroup( Integer orderNumber, String organizationId);

    @Query("""
    select l
    from Level l
    where l.organizationId = :organizationId
      and l.deleted = false
    order by l.orderNumber asc
""")
    Optional<Level> getFirstLevelForGroup(
            @Param("organizationId") String organizationId
    );
}
