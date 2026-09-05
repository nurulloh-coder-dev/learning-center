package org.example.crm.service;

import lombok.RequiredArgsConstructor;
import org.example.crm.entity.analyticsRecord.*;
import org.example.crm.projection.*;
import org.example.crm.repository.*;
import org.example.crm.validator.UserValidator;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.Month;
import java.time.YearMonth;

@Service
@RequiredArgsConstructor
public class AnalyticService {

    private final BranchRepository branchRepository;
    private final EnrollmentRepository enrollmentRepository;
    private final InvoiceRepository invoiceRepository;
    private final LeadRepository leadRepository;
    private final StudentRepository studentRepository;
    private final TeacherRepository teacherRepository;
    private final UserValidator userValidator;

    private record DateRange(LocalDateTime prevMonthStart, LocalDateTime currentMonthStart, LocalDateTime nextMonthStart) {}

    private DateRange resolveDateRange(Integer year, Month month) {
        int filterYear = year == null ? LocalDateTime.now().getYear() : year;
        Month filterMonth = month == null ? LocalDateTime.now().getMonth() : month;
        YearMonth ym = YearMonth.of(filterYear, filterMonth);

        return new DateRange(
                ym.minusMonths(1).atDay(1).atStartOfDay(),
                ym.atDay(1).atStartOfDay(),
                ym.plusMonths(1).atDay(1).atStartOfDay()
        );
    }

    private double calculatePercentageDifference(double current, double previous) {
        return previous != 0.0 ? ((current - previous) / previous) * 100.0 : 0.0;
    }

    private long defaultIfNull(Long val) {
        return val == null ? 0L : val;
    }

    private double defaultIfNull(Double val) {
        return val == null ? 0.0 : val;
    }

    public AnalyticBranch getBranch() {
        String organizationId = userValidator.authenticateAndGetOrganizationId();
        AnalyticBranchProjection projection = branchRepository.getAnalyticBranch(organizationId);
        return new AnalyticBranch(defaultIfNull(projection != null ? projection.getBranchCount() : null));
    }

    public AnalyticEnrollment getEnrollment(Integer year, Month month) {
        String organizationId = userValidator.authenticateAndGetOrganizationId();
        DateRange range = resolveDateRange(year, month);

        AnalyticEnrollmentProjection projection = enrollmentRepository.getAnalyticEnrollment(
                organizationId, range.prevMonthStart(), range.currentMonthStart(), range.nextMonthStart()
        );

        long total = defaultIfNull(projection != null ? projection.getEnrollmentCount() : null);
        long current = defaultIfNull(projection != null ? projection.getEnrollmentCountInMonth() : null);
        long previous = defaultIfNull(projection != null ? projection.getEnrollmentCountInPreviousMonth() : null);
        double difference = calculatePercentageDifference(current, previous);

        return new AnalyticEnrollment(total, current, previous, difference);
    }

    public AnalyticInvoice getInvoice(Integer year, Month month) {
        String organizationId = userValidator.authenticateAndGetOrganizationId();
        DateRange range = resolveDateRange(year, month);

        AnalyticInvoiceProjection projection = invoiceRepository.getAnalyticInvoice(
                organizationId, range.prevMonthStart(), range.currentMonthStart(), range.nextMonthStart()
        );

        double total = defaultIfNull(projection != null ? projection.getInvoiceAmount() : null);
        double current = defaultIfNull(projection != null ? projection.getInvoiceAmountInMonth() : null);
        double previous = defaultIfNull(projection != null ? projection.getInvoiceAmountInPreviousMonth() : null);
        double difference = calculatePercentageDifference(current, previous);

        return new AnalyticInvoice(total, current, previous, difference);
    }

    public AnalyticLead getLead(Integer year, Month month) {
        String organizationId = userValidator.authenticateAndGetOrganizationId();
        DateRange range = resolveDateRange(year, month);

        AnalyticLeadProjection projection = leadRepository.getAnalyticLead(
                organizationId, range.prevMonthStart(), range.currentMonthStart(), range.nextMonthStart()
        );

        long total = defaultIfNull(projection != null ? projection.getLeadCount() : null);
        long current = defaultIfNull(projection != null ? projection.getLeadCountInMonth() : null);
        long previous = defaultIfNull(projection != null ? projection.getLeadCountInPrevMonth() : null);
        double difference = calculatePercentageDifference(current, previous);

        return new AnalyticLead(total, current, previous, difference);
    }

    public AnalyticStudent getStudent(Integer year, Month month) {
        String organizationId = userValidator.authenticateAndGetOrganizationId();
        DateRange range = resolveDateRange(year, month);

        AnalyticStudentProjection projection = studentRepository.getAnalyticStudent(
                organizationId, range.prevMonthStart(), range.currentMonthStart(), range.nextMonthStart()
        );

        long total = defaultIfNull(projection != null ? projection.getStudentCount() : null);
        long current = defaultIfNull(projection != null ? projection.getStudentsAddedInMonth() : null);
        long previous = defaultIfNull(projection != null ? projection.getStudentsAddedInPrevMonth() : null);
        double difference = calculatePercentageDifference(current, previous);

        return new AnalyticStudent(total, current, previous, difference);
    }

    public AnalyticTeacher getTeacher(Integer year, Month month) {
        String organizationId = userValidator.authenticateAndGetOrganizationId();
        DateRange range = resolveDateRange(year, month);

        AnalyticTeacherProjection projection = teacherRepository.getAnalyticTeacher(
                organizationId, range.prevMonthStart(), range.currentMonthStart(), range.nextMonthStart()
        );

        long total = defaultIfNull(projection != null ? projection.getTeacherCount() : null);
        long current = defaultIfNull(projection != null ? projection.getTeachersAddedInMonth() : null);
        long previous = defaultIfNull(projection != null ? projection.getTeachersAddedInPrevMonth() : null);
        double difference = calculatePercentageDifference(current, previous);

        return new AnalyticTeacher(total, current, previous, difference);
    }
}