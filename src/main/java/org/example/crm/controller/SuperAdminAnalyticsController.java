package org.example.crm.controller;

import lombok.RequiredArgsConstructor;
import org.example.crm.annotation.CurrentUser;
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
    public ResponseEntity<AnalyticBranch> branch(@CurrentUser User user){
        return ResponseEntity.ok(service.getBranch(user));
    }

    @GetMapping("/enrollment")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public ResponseEntity<AnalyticEnrollment> enrollment(@CurrentUser User user,
                                                 @RequestParam(required = false) Integer year,
                                                 @RequestParam(required = false) Month month){
        return ResponseEntity.ok(service.getEnrollment(user,year, month));
    }

    @GetMapping("/invoice")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public ResponseEntity<AnalyticInvoice> invoice(@CurrentUser User user,
                                                   @RequestParam(required = false) Integer year,
                                                   @RequestParam(required = false) Month month){
        return ResponseEntity.ok(service.getInvoice(user,year, month));
    }

    @GetMapping("/lead")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public ResponseEntity<AnalyticLead> lead(@CurrentUser User user,
                                             @RequestParam(required = false) Integer year,
                                             @RequestParam(required = false) Month month){
        return ResponseEntity.ok(service.getLead(user,year, month));
    }

    @GetMapping("/student")
    public ResponseEntity<AnalyticStudent> student(@CurrentUser User user,
                                                   @RequestParam(required = false) Integer year,
                                                   @RequestParam(required = false) Month month){
        return ResponseEntity.ok(service.getStudent(user,year, month));
    }

    @GetMapping("/teacher")
    public ResponseEntity<AnalyticTeacher> teacher(@CurrentUser User user,
                                                   @RequestParam(required = false) Integer year,
                                                   @RequestParam(required = false) Month month){
        return ResponseEntity.ok(service.getTeacher(user,year, month));
    }






}
