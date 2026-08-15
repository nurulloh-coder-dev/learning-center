package org.example.crm.repository;

import org.example.crm.entity.enums.InvoiceStatus;
import org.example.crm.entity.model.Invoice;
import org.example.crm.projection.InvoiceProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface InvoiceRepository extends JpaRepository<Invoice, String> {
    @Query("""
        select
            i.id as id,
            i.invoiceNumber as invoiceNumber,
            s.id as studentId,
            su.id as studentUserId,
            su.imageUrl as studentImageUrl,
            su.fullName as studentFullName,
            su.phone as studentPhone,
            su.birthDate as studentBirthDate,
            su.role as studentRole,
            s.parentPhone as parentPhone,
            g.id as groupId,
            g.name as groupName,
            g.room as groupRoom,
            t.id as teacherId,
            tu.id as teacherUserId,
            tu.imageUrl as teacherImageUrl,
            tu.fullName as teacherFullName,
            tu.phone as teacherPhone,
            tu.birthDate as teacherBirthDate,
            tu.role as teacherRole,
            i.amount as amount,
            i.issuedAt as issuedAt,
            i.paymentStatus as status,
            tt.id as timeTableId,
            tt.dayType as timeTableDayType,
            tt.startTime as timeTableStartTime,
            tt.endTime as timeTableEndTime,
            g.status as groupStatus
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
    boolean existsByInvoiceNumber(String invoiceNumber);

    List<Invoice> findInvoicesByPaymentStatus(InvoiceStatus paymentStatus);

    @Query("""
        update Invoice i
        set i.paymentStatus = :newStatus
        where i.paymentStatus = :oldStatus
          and i.issuedAt <= :now
""")
    long findInvoicesByPaymentStatusAnd2DaysOld(InvoiceStatus oldStatus,InvoiceStatus newStatus, LocalDate now);
}
