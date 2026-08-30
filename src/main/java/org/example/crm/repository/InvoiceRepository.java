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
            tu.branch.id as branchId,
            s.id as studentId,
            su.imageUrl as studentImageUrl,
            su.id as studentUserId,
            su.fullName as studentFullName,
            su.phone as studentPhone,
            su.birthDate as studentBirthDate,
            su.role as studentRole,
            s.parentPhone as parentPhone,
            tu.role as teacherRole,
            i.amount as amount,
            i.issuedAt as issuedAt,
            i.paymentStatus as status,
            i.type as type
        from Invoice i
        join i.student s
        join s.user su
        left join Enrollment e on s.id = e.student.id
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
        update Invoice i
        set i.paymentStatus = :newStatus
        where i.paymentStatus = :oldStatus
          and i.issuedAt <= :now
""")
    long findInvoicesByPaymentStatusAnd2DaysOld(InvoiceStatus oldStatus,InvoiceStatus newStatus, LocalDate now);


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
