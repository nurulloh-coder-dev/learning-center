package org.example.crm.repository;

import jakarta.transaction.Transactional;
import org.example.crm.entity.enums.InvoiceStatus;
import org.example.crm.entity.model.Invoice;
import org.example.crm.projection.AnalyticInvoiceProjection;
import org.example.crm.projection.InvoiceProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface InvoiceRepository extends JpaRepository<Invoice, String> {
    @Query("""
        select
            i.id as id,
            i.invoiceNumber as invoiceNumber,
            e.id as enrollmentId,
            s.id as studentId,
            su.fullName as fullName,
            g.id as groupId,
            e.leavingReason as reason,
            i.amount as amount,
            i.createdAt as createAt
        from Invoice i
        join i.enrollment e
        join e.student s
        join s.user su
        left join Group g on e.group.id = g.id
        left join g.teacher t
        left join t.user tu
        left join g.timeTable tt
        where i.deleted = false
        and (:search is null
            or su.fullName ilike :search
            or su.phone ilike :search
            or g.name ilike :search
            or i.invoiceNumber ilike :search )
        and (i.createdAt >= :from)
        and (i.createdAt <= :to)
        and (:status is null or i.paymentStatus = :status)
    """)
    Page<InvoiceProjection> getAllInvoicesByFilter(
            @Param("search") String search,
            @Param("from") LocalDateTime from,
            @Param("to") LocalDateTime to,
            @Param("status") InvoiceStatus status,
            Pageable pageable
    );


    @Query("""
        select
           coalesce(sum(i.amount), 0) as invoiceAmount,
                   coalesce(
                       sum(
                           case
                               when i.createdAt >= :month and i.createdAt <=:nextMonth then i.amount
                               else 0
                           end
                       ),
                       0
           ) as invoiceAmountInMonth,
           coalesce(
                sum(
                    case
                        when i.createdAt >= :prevMonth and i.createdAt <= :month then i.amount
                        else 0
                    end
                )
           ) as invoiceAmountInPreviousMonth
           from Invoice i
           where i.organizationId = :organizationId
           and i.deleted = false
           and i.paymentStatus = 'PAID'
""")
    AnalyticInvoiceProjection getAnalyticInvoice(String organizationId,
                                                 LocalDateTime prevMonth,
                                                 LocalDateTime month,
                                                 LocalDateTime nextMonth);

    @Query("select exists(select i.id from Invoice i where i.id=:id and i.organizationId=:orgId)")
    boolean checkId(String id,@Param("orgId")String organizationId);

    @Modifying
    @Transactional
    @Query("update Invoice i set i.deleted = true where i.id =:id")
    void softDelete(String id);

    @Query("""
           select exists (
                      select i.id from Invoice i
                      join i.enrollment e
                      where e.group.id=:groupId
                      and i.level=:levelName
                      and i.month=:month
                      and i.deleted=false)""")
    boolean checkIfAlreadyCreated(String groupId, String levelName, Integer currentMonth);
}
