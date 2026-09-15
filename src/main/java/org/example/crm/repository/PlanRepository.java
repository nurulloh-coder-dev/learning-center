package org.example.crm.repository;

import jakarta.transaction.Transactional;
import org.example.crm.entity.model.Plan;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface PlanRepository extends JpaRepository<Plan, String> {

    boolean existsByCode(String code);

    @Query("""
        select p from Plan p
        where (:search is null or lower(p.name) like lower(concat('%', :search, '%')))
          and p.deleted = false
        """)
    Page<Plan> findAll(String search, Pageable pageable);

    @Query("select count(s) > 0 from Subscription s where s.plan.id = :planId and s.status in ('ACTIVE','GRACE') and s.deleted = false")
    boolean hasActiveSubscriptions(String planId);


    @Modifying
    @Transactional
    @Query("update Plan p set p.deleted=true where p.id=:id")
    void softDelete(String id);
}