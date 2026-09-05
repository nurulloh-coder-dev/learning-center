package org.example.crm.repository;

import org.example.crm.entity.enums.InvoiceStatus;
import org.example.crm.entity.model.Invoice;
import org.example.crm.projection.AnalyticInvoiceProjection;
import org.example.crm.projection.InvoiceProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Repository
public interface InvoiceRepository extends JpaRepository<Invoice, String> {
    @Query("""
        select
            i.id as id,
            i.invoiceNumber as invoiceNumber,
    
            e.id as enrollmentId,
            s.id as studentId,
            g.id as groupId,
            e.leavingReason as reason,
            e.monthlyFee as monthlyFee,
            e.paidAmount as paidAmount,
            e.status as enrollmentStatus,
    
            i.amount as amount,
            i.issuedAt as issuedAt,
            i.paymentStatus as status,
            i.type as type
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
        and (i.issuedAt >= :from)
        and (i.issuedAt <= :to)
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
}
