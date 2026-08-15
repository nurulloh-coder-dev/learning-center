package org.example.learningcenter.initializer;

import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.learningcenter.entity.enums.*;
import org.example.learningcenter.entity.model.*;
import org.example.learningcenter.repository.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Component
@RequiredArgsConstructor ////implements CommandLineRunner
public class DataInitializer{

    final UserRepository userRepository;
    final TeacherRepository teacherRepository;
    final TimeTableRepository timeTableRepository;
    final GroupRepository groupRepository;
    final StudentRepository studentRepository;
    final LessonRepository lessonRepository;
    final InvoiceRepository invoiceRepository;
    final AttendanceRepository attendanceRepository;
    final PasswordEncoder passwordEncoder;
    final EntityManager entityManager;
//
//    @Override
    @Transactional
    public void run(String... args) {
//        if (userRepository.count() > 0) return;

        String encodedPassword = passwordEncoder.encode("root1234");

        // ============ USERS ============
        User adminUser = new User();
        adminUser.setFullName("Admin John");
        adminUser.setPhone("1");
        adminUser.setPassword(encodedPassword);
        adminUser.setRole(Role.ADMINISTRATOR);
        adminUser.setBirthDate(LocalDate.of(1990, 1, 1));
        userRepository.save(adminUser);

        User teacherUser1 = new User();
        teacherUser1.setFullName("Alice Teacher");
        teacherUser1.setPhone("2");
        teacherUser1.setPassword(encodedPassword);
        teacherUser1.setRole(Role.TEACHER);
        teacherUser1.setBirthDate(LocalDate.of(1992, 5, 10));
        userRepository.save(teacherUser1);

        User teacherUser2 = new User();
        teacherUser2.setFullName("Bob Teacher");
        teacherUser2.setPhone("+998901234569");
        teacherUser2.setPassword(encodedPassword);
        teacherUser2.setRole(Role.TEACHER);
        teacherUser2.setBirthDate(LocalDate.of(1988, 8, 20));
        userRepository.save(teacherUser2);

        User studentUser1 = new User();
        studentUser1.setFullName("Charlie Student");
        studentUser1.setPhone("+998901234570");
        studentUser1.setPassword(encodedPassword);
        studentUser1.setRole(Role.STUDENT);
        studentUser1.setBirthDate(LocalDate.of(2010, 3, 5));
        userRepository.save(studentUser1);

        User studentUser2 = new User();
        studentUser2.setFullName("Diana Student");
        studentUser2.setPhone("+998901234571");
        studentUser2.setPassword(encodedPassword);
        studentUser2.setRole(Role.STUDENT);
        studentUser2.setBirthDate(LocalDate.of(2011, 7, 15));
        userRepository.save(studentUser2);

        User studentUser3 = new User();
        studentUser3.setFullName("Eve Student");
        studentUser3.setPhone("+998901234572");
        studentUser3.setPassword(encodedPassword);
        studentUser3.setRole(Role.STUDENT);
        studentUser3.setBirthDate(LocalDate.of(2009, 11, 25));
        userRepository.save(studentUser3);

        User studentUser4 = new User();
        studentUser4.setFullName("Frank Student");
        studentUser4.setPhone("+998901234573");
        studentUser4.setPassword(encodedPassword);
        studentUser4.setRole(Role.STUDENT);
        studentUser4.setBirthDate(LocalDate.of(2012, 1, 30));
        userRepository.save(studentUser4);

        // ============ TEACHERS ============
        Teacher teacher1 = new Teacher();
            teacher1.setUser(teacherUser1);
        teacherRepository.save(teacher1);

        Teacher teacher2 = new Teacher();
        teacher2.setUser(teacherUser2);
        teacherRepository.save(teacher2);

        // ============ TIMETABLES ============
        TimeTable tt1 = new TimeTable();
        tt1.setDayType(DayType.ODD);
        tt1.setStartTime(LocalTime.of(9, 0));
        tt1.setEndTime(LocalTime.of(11, 0));
        timeTableRepository.save(tt1);

        TimeTable tt2 = new TimeTable();
        tt2.setDayType(DayType.EVEN);
        tt2.setStartTime(LocalTime.of(14, 0));
        tt2.setEndTime(LocalTime.of(16, 0));
        timeTableRepository.save(tt2);

        // ============ GROUPS ============
        Group group1 = new Group();
        group1.setName("Math Group A");
        group1.setRoom("Room 101");
        group1.setTeacher(teacher1);
        group1.setTimeTable(tt1);
        group1.setStatus(GroupStatus.ONGOING);
        groupRepository.save(group1);

        Group group2 = new Group();
        group2.setName("English Group B");
        group2.setRoom("Room 202");
        group2.setTeacher(teacher2);
        group2.setTimeTable(tt2);
        group2.setStatus(GroupStatus.STARTING);
        groupRepository.save(group2);

        // ============ STUDENTS ============
        Student student1 = new Student();
        student1.setUser(studentUser1);
        student1.setParentPhone("+998901234580");
        studentRepository.save(student1);

        Student student2 = new Student();
        student2.setUser(studentUser2);
        student2.setParentPhone("+998901234581");
        studentRepository.save(student2);

        Student student3 = new Student();
        student3.setUser(studentUser3);
        student3.setParentPhone("+998901234582");
        studentRepository.save(student3);

        Student student4 = new Student();
        student4.setUser(studentUser4);
        student4.setParentPhone("+998901234583");
        studentRepository.save(student4);

        // ============ LESSONS ============
        Lesson lesson1 = new Lesson();
        lesson1.setLessonName("Algebra Basics");
        lesson1.setIsCompleted(true);
        lesson1.setGroup(group1);
        lesson1.setTeacher(teacher1);
        lessonRepository.save(lesson1);

        Lesson lesson2 = new Lesson();
        lesson2.setLessonName("Geometry Intro");
        lesson2.setIsCompleted(false);
        lesson2.setGroup(group1);
        lesson2.setTeacher(teacher1);
        lessonRepository.save(lesson2);

        Lesson lesson3 = new Lesson();
        lesson3.setLessonName("Grammar Fundamentals");
        lesson3.setIsCompleted(true);
        lesson3.setGroup(group2);
        lesson3.setTeacher(teacher2);
        lessonRepository.save(lesson3);

        Lesson lesson4 = new Lesson();
        lesson4.setLessonName("Reading Comprehension");
        lesson4.setIsCompleted(false);
        lesson4.setGroup(group2);
        lesson4.setTeacher(teacher2);
        lessonRepository.save(lesson4);

        // ============ ENROLLMENTS ============
        Enrollment enrollment1 = new Enrollment();
        enrollment1.setStudent(student1);
        enrollment1.setGroup(group1);
        entityManager.persist(enrollment1);

        Enrollment enrollment2 = new Enrollment();
        enrollment2.setStudent(student2);
        enrollment2.setGroup(group1);
        entityManager.persist(enrollment2);

        Enrollment enrollment3 = new Enrollment();
        enrollment3.setStudent(student3);
        enrollment3.setGroup(group2);
        entityManager.persist(enrollment3);

        Enrollment enrollment4 = new Enrollment();
        enrollment4.setStudent(student4);
        enrollment4.setGroup(group2);
        entityManager.persist(enrollment4);

        // ============ INVOICES ============
        Invoice invoice1 = new Invoice();
        invoice1.setInvoiceNumber("INV-001");
        invoice1.setAmount(new BigDecimal("150.00"));
        invoice1.setPaymentStatus(InvoiceStatus.PAID);
        invoice1.setIssuedAt(LocalDateTime.now().minusMonths(2));
        invoice1.setStudent(student1);
        invoiceRepository.save(invoice1);

        Invoice invoice2 = new Invoice();
        invoice2.setInvoiceNumber("INV-002");
        invoice2.setAmount(new BigDecimal("150.00"));
        invoice2.setPaymentStatus(InvoiceStatus.PENDING);
        invoice2.setIssuedAt(LocalDateTime.now().minusDays(5));
        invoice2.setStudent(student2);
        invoiceRepository.save(invoice2);

        Invoice invoice3 = new Invoice();
        invoice3.setInvoiceNumber("INV-003");
        invoice3.setAmount(new BigDecimal("200.00"));
        invoice3.setPaymentStatus(InvoiceStatus.OVERDUE);
        invoice3.setIssuedAt(LocalDateTime.now().minusMonths(1));
        invoice3.setStudent(student3);
        invoiceRepository.save(invoice3);

        Invoice invoice4 = new Invoice();
        invoice4.setInvoiceNumber("INV-004");
        invoice4.setAmount(new BigDecimal("200.00"));
        invoice4.setPaymentStatus(InvoiceStatus.PENDING);
        invoice4.setIssuedAt(LocalDateTime.now().minusDays(3));
        invoice4.setStudent(student4);
        invoiceRepository.save(invoice4);

        // ============ ATTENDANCES ============
        Attendance attendance1 = new Attendance();
        attendance1.setLesson(lesson1);
        attendance1.addStudentAttendance(createAttendanceStudent(student1, AttendanceStatus.PRESENT));
        attendanceRepository.save(attendance1);

        Attendance attendance2 = new Attendance();
        attendance2.setLesson(lesson3);
        attendance2.addStudentAttendance(createAttendanceStudent(student3, AttendanceStatus.ABSENT));
        attendance2.addStudentAttendance(createAttendanceStudent(student4, AttendanceStatus.PRESENT));
        attendanceRepository.save(attendance2);
    }

    private AttendanceStudent createAttendanceStudent(Student student, AttendanceStatus status) {
        AttendanceStudent as = new AttendanceStudent();
        as.setStudent(student);
        as.setStatus(status);
        return as;
    }
}
