package org.example.learningcenter.cron;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.learningcenter.entity.dto.InvoiceCreateDto;
import org.example.learningcenter.entity.enums.InvoiceStatus;
import org.example.learningcenter.entity.model.Invoice;
import org.example.learningcenter.entity.model.Student;
import org.example.learningcenter.repository.GroupRepository;
import org.example.learningcenter.repository.InvoiceRepository;
import org.example.learningcenter.repository.StudentRepository;
import org.example.learningcenter.service.InvoiceService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Component
@Slf4j
@RequiredArgsConstructor
public class InvoiceCron {
    final InvoiceService invoiceService;
    final GroupRepository groupRepository;
    final StudentRepository studentRepository;
    final InvoiceRepository invoiceRepository;
    @Value("${spring.application.invoiceAmount}")
    private BigDecimal invoiceAmount;

    @Scheduled(cron = "0 0 7 * * *", zone = "Asia/Tashkent")
    public void sendAndCreateInvoices() {
       List<Student> students = studentRepository.findAllStudentsForInvoice();
       students.forEach(student -> {
           invoiceService.create(new InvoiceCreateDto(
                   student.getId(),
                   invoiceAmount
           ));
       });
        int size = students.size();
        log.info("{} Invoices have been created and sent to students", size);
        ///send invoice to students
    }

    @Scheduled(cron = "0 1 7 * * *", zone = "Asia/Tashkent")
    public void updateStatusToOverDue() {
        LocalDate now = LocalDate.now().minusDays(2);
        long invoices = invoiceRepository.findInvoicesByPaymentStatusAnd2DaysOld(InvoiceStatus.PENDING, InvoiceStatus.OVERDUE, now);
        log.info("{} Invoice's status have been set to OVERDUE", invoices);
    }
}
