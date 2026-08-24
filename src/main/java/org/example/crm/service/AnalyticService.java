package org.example.crm.service;

import lombok.RequiredArgsConstructor;
import org.example.crm.entity.analyticsRecord.*;
import org.example.crm.entity.model.User;
import org.example.crm.projection.*;
import org.example.crm.repository.*;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

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

    public AnalyticEnrollment getEnrollment(User user) {
        LocalDateTime monthAgo = LocalDateTime.now().minusMonths(1);
        AnalyticEnrollmentProjection projection = enrollmentRepository.getAnalyticEnrollment(user.getOrganizationId(),monthAgo);

        return new AnalyticEnrollment(projection.getEnrollmentCount(), projection.getEnrollmentCount());
    }

    public AnalyticInvoice getInvoice(User user) {
        LocalDateTime monthAgo = LocalDateTime.now().minusMonths(1);
        AnalyticInvoiceProjection projection = invoiceRepository.getAnalyticInvoice(user.getOrganizationId(),monthAgo);
        return new AnalyticInvoice(projection.getInvoiceAmount(),projection.getInvoiceAmountInMonth());
    }

    public AnalyticLead getLead(User user) {
        LocalDateTime monthAgo = LocalDateTime.now().minusMonths(1);
        AnalyticLeadProjection projection = leadRepository.getAnalyticLead(user.getOrganizationId(), monthAgo);
        return new AnalyticLead(projection.getLeadCount(),projection.getLeadCountInAMonth());
    }

    public AnalyticStudent getStudent(User user) {
        LocalDateTime  monthAgo = LocalDateTime.now().minusMonths(1);
        AnalyticStudentProjection projection = studentRepository.getAnalyticStudent(user.getOrganizationId(),monthAgo);
        return  new AnalyticStudent(projection.getStudentCount(),projection.getStudentCount());
    }

    public AnalyticTeacher getTeacher(User user) {
        LocalDateTime  monthAgo = LocalDateTime.now().minusMonths(1);
        AnalyticTeacherProjection projection = teacherRepository.getAnalyticTeacher(user.getOrganizationId(),monthAgo);
        return  new AnalyticTeacher(projection.getTeacherCount(),projection.getTeacherCount());

    }
}
