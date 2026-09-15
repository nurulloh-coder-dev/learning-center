package org.example.crm.repository;

import org.example.crm.entity.enums.SubscriptionStatus;
import org.example.crm.entity.model.Subscription;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

public interface SubscriptionRepository extends JpaRepository<Subscription, String> {

    @Query("""
        select s from Subscription s
        join fetch s.plan
        where s.organization.id = :organizationId
          and s.status in ('ACTIVE', 'GRACE')
          and s.deleted = false
        """)
    Optional<Subscription> findCurrent(String organizationId);

    @Query("""
        select s from Subscription s
        join s.organization o
        where (:search is null or lower(o.name) like lower(concat('%', :search, '%')))
          and (:status is null or s.status = :status)
          and s.deleted = false
        """)
    Page<Subscription> findAll(String search, SubscriptionStatus status, Pageable pageable);

    List<Subscription> findAllByStatusAndExpiresAtBefore(SubscriptionStatus status, Instant instant);
}