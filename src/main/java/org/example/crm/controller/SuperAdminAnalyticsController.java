package org.example.crm.controller;

import lombok.RequiredArgsConstructor;
import org.example.crm.annotation.CurrentUser;
import org.example.crm.config.CustomUserDetails;
import org.example.crm.entity.analyticsRecord.*;
import org.example.crm.entity.model.Invoice;
import org.example.crm.entity.model.Lead;
import org.example.crm.entity.model.Student;
import org.example.crm.entity.model.User;
import org.example.crm.service.AnalyticService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.Month;

@RestController
@RequestMapping("api/v1/analytics")
@RequiredArgsConstructor
public class SuperAdminAnalyticsController {
    final AnalyticService service;


    @GetMapping("/branch")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public ResponseEntity<AnalyticBranch> branch() {
        return ResponseEntity.ok(service.getBranch());
    }

    @GetMapping("/enrollment")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public ResponseEntity<AnalyticEnrollment> enrollment(@RequestParam(required = false) Integer year,
                                                         @RequestParam(required = false) Month month) {
        return ResponseEntity.ok(service.getEnrollment(year, month));
    }

    @GetMapping("/invoice")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public ResponseEntity<AnalyticInvoice> invoice(@RequestParam(required = false) Integer year,
                                                   @RequestParam(required = false) Month month) {
        return ResponseEntity.ok(service.getInvoice(year, month));
    }

    @GetMapping("/lead")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public ResponseEntity<AnalyticLead> lead(@RequestParam(required = false) Integer year,
                                             @RequestParam(required = false) Month month) {
        return ResponseEntity.ok(service.getLead(year, month));
    }

    @GetMapping("/student")
    public ResponseEntity<AnalyticStudent> student(@RequestParam(required = false) Integer year,
                                                   @RequestParam(required = false) Month month) {
        return ResponseEntity.ok(service.getStudent(year, month));
    }

    @GetMapping("/teacher")
    public ResponseEntity<AnalyticTeacher> teacher(@RequestParam(required = false) Integer year,
                                                   @RequestParam(required = false) Month month) {
        return ResponseEntity.ok(service.getTeacher(year, month));
    }


}
