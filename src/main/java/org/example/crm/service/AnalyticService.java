package org.example.crm.service;

import lombok.RequiredArgsConstructor;
import org.example.crm.entity.analyticsRecord.*;
import org.example.crm.entity.model.User;
import org.example.crm.projection.*;
import org.example.crm.repository.*;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.Month;
import java.time.YearMonth;

@Service
@RequiredArgsConstructor
public class AnalyticService {
    final BranchRepository branchRepository;
    final EnrollmentRepository enrollmentRepository;
    final InvoiceRepository invoiceRepository;
    final LeadRepository leadRepository;
    final StudentRepository studentRepository;
    final TeacherRepository teacherRepository;

    public AnalyticBranch getBranch(User user) {

        AnalyticBranchProjection projection = branchRepository.getAnalyticBranch(user.getOrganizationId());
        return new AnalyticBranch(projection.getBranchCount());
    }

    public AnalyticEnrollment getEnrollment(User user, Integer year, Month month) {
        int filterYear = year == null ? LocalDateTime.now().getYear() : year;
        Month filterMonth = month ==null ? LocalDateTime.now().getMonth() : month;
        YearMonth yearMonth = YearMonth.of(filterYear, filterMonth);

        LocalDateTime startOfPreviousMonth = yearMonth
                .minusMonths(1)
                .atDay(1)
                .atStartOfDay();

        LocalDateTime startOfMonth = yearMonth
                .atDay(1)
                .atStartOfDay();

        LocalDateTime startOfNextMonth = yearMonth.plusMonths(1)
                .atDay(1)
                .atStartOfDay();
        AnalyticEnrollmentProjection projection = enrollmentRepository.getAnalyticEnrollment(user.getOrganizationId(),startOfPreviousMonth,startOfMonth,startOfNextMonth);

        Long current = projection.getEnrollmentCountInMonth();
        Long previous = projection.getEnrollmentCountInPreviousMonth();

        Double difference = 0.0;

        if (previous != 0) {
            difference = ((double) (current - previous) / previous) * 100;
        }

        return new AnalyticEnrollment(projection.getEnrollmentCount(), current, previous, difference);
    }

    public AnalyticInvoice getInvoice(User user, Integer year, Month month) {
        int filterYear = year == null ? LocalDateTime.now().getYear() : year;
        Month filterMonth = month ==null ? LocalDateTime.now().getMonth() : month;
        YearMonth yearMonth = YearMonth.of(filterYear, filterMonth);


        LocalDateTime startOfPreviousMonth = yearMonth
                .minusMonths(1)
                .atDay(1)
                .atStartOfDay();

        LocalDateTime startOfMonth = yearMonth
                .atDay(1)
                .atStartOfDay();

        LocalDateTime startOfNextMonth = yearMonth.plusMonths(1)
                .atDay(1)
                .atStartOfDay();
        AnalyticInvoiceProjection projection = invoiceRepository.getAnalyticInvoice(user.getOrganizationId(),startOfPreviousMonth,startOfMonth,startOfNextMonth);


        Double current = projection.getInvoiceAmountInMonth();
        Double previous = projection.getInvoiceAmountInPreviousMonth();

        double difference = 0.0;

        if (previous != 0) {
            difference = ( (current - previous) / previous) * 100;
        }

        return new AnalyticInvoice(projection.getInvoiceAmount(),current,previous,difference);
    }

    public AnalyticLead getLead(User user, Integer year, Month month) {
        int filterYear = year == null ? LocalDateTime.now().getYear() : year;
        Month filterMonth = month ==null ? LocalDateTime.now().getMonth() : month;
        YearMonth yearMonth = YearMonth.of(filterYear, filterMonth);

        LocalDateTime startOfPreviousMonth = yearMonth
                .minusMonths(1)
                .atDay(1)
                .atStartOfDay();


        LocalDateTime startOfMonth = yearMonth
                .atDay(1)
                .atStartOfDay();

        LocalDateTime startOfNextMonth = yearMonth.plusMonths(1)
                .atDay(1)
                .atStartOfDay();
        AnalyticLeadProjection projection = leadRepository.getAnalyticLead(user.getOrganizationId(),startOfPreviousMonth, startOfMonth,startOfNextMonth);



        Long current = projection.getLeadCountInMonth();
        Long previous = projection.getLeadCountInPrevMonth();

        double difference = 0.0;

        if (previous != 0) {
            difference = ((double) (current - previous) / previous) * 100;
        }

        return new AnalyticLead(projection.getLeadCount(),current, previous,difference);
    }

    public AnalyticStudent getStudent(User user, Integer year, Month month) {
        int filterYear = year == null ? LocalDateTime.now().getYear() : year;
        Month filterMonth = month ==null ? LocalDateTime.now().getMonth() : month;
        YearMonth yearMonth = YearMonth.of(filterYear, filterMonth);

        LocalDateTime startOfPreviousMonth = yearMonth
                .minusMonths(1)
                .atDay(1)
                .atStartOfDay();


        LocalDateTime startOfMonth = yearMonth
                .atDay(1)
                .atStartOfDay();

        LocalDateTime startOfNextMonth = yearMonth.plusMonths(1)
                .atDay(1)
                .atStartOfDay();
        AnalyticStudentProjection projection = studentRepository.getAnalyticStudent(user.getOrganizationId(),startOfPreviousMonth,startOfMonth,startOfNextMonth);




        Long current = projection.getStudentsAddedInMonth();
        Long previous = projection.getStudentsAddedInPrevMonth();

        double difference = 0.0;

        if (previous != 0) {
            difference = ((double) (current - previous) / previous) * 100;
        }

        return  new AnalyticStudent(projection.getStudentCount(),current,previous,difference);
    }

    public AnalyticTeacher getTeacher(User user, Integer year, Month month) {
        int filterYear = year == null ? LocalDateTime.now().getYear() : year;
        Month filterMonth = month ==null ? LocalDateTime.now().getMonth() : month;
        YearMonth yearMonth = YearMonth.of(filterYear, filterMonth);

        LocalDateTime startOfPreviousMonth = yearMonth
                .minusMonths(1)
                .atDay(1)
                .atStartOfDay();


        LocalDateTime startOfMonth = yearMonth
                .atDay(1)
                .atStartOfDay();

        LocalDateTime startOfNextMonth = yearMonth.plusMonths(1)
                .atDay(1)
                .atStartOfDay();
        AnalyticTeacherProjection projection = teacherRepository.getAnalyticTeacher(user.getOrganizationId(), startOfPreviousMonth,startOfMonth,startOfNextMonth);


        Long current = projection.getTeachersAddedInMonth();
        Long previous = projection.getTeachersAddedInPrevMonth();

        double difference = 0.0;

        if (previous != 0) {
            difference = ((double) (current - previous) / previous) * 100;
        }

        return  new AnalyticTeacher(projection.getTeacherCount(),current,previous,difference);

    }
}
