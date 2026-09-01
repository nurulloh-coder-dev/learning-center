package org.example.crm.service;

import org.example.crm.entity.dto.attendance.*;
import org.example.crm.entity.dto.attendanceStudent.AttendanceStudentCreateDto;
import org.example.crm.entity.dto.attendanceStudent.AttendanceStudentUpdateDto;
import org.example.crm.entity.dto.attendanceStudent.StatusReasonDto;
import org.example.crm.entity.model.Attendance;
import org.example.crm.entity.model.AttendanceStudent;
import org.example.crm.entity.model.Lesson;
import org.example.crm.entity.model.Student;
import org.example.crm.mapper.AttendanceMapper;
import org.example.crm.projection.AttendanceProjection;
import org.example.crm.projection.AttendanceStudentProjection;
import org.example.crm.projection.MyAttendanceProjection;
import org.example.crm.repository.AttendanceRepository;
import org.example.crm.validator.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class AttendanceService extends AbstractService<
        AttendanceRepository,
        AttendanceMapper,
        AttendanceValidator> implements CrudService<AttendanceCreateDto, AttendanceUpdateDto, AttendanceDto, String> {

    private final LessonValidator lessonValidator;
    private final StudentValidator studentValidator;
    private final UserValidator userValidator;
    private final GroupValidator groupValidator;

    protected AttendanceService(AttendanceRepository repository, AttendanceMapper mapper, AttendanceValidator validator, LessonValidator lessonValidator, StudentValidator studentValidator, UserValidator userValidator, GroupValidator groupValidator) {
        super(repository, mapper, validator);
        this.lessonValidator = lessonValidator;
        this.studentValidator = studentValidator;
        this.userValidator = userValidator;
        this.groupValidator = groupValidator;
    }

    @Override
    public Page<AttendanceDto> getAll(Pageable pageable, String search) {
        Page<Attendance> all = repository.findAll(pageable, search);
        return all.map(mapper::toDto);
    }

    @Override
    public AttendanceDto get(String id) {
        Attendance attendance = validator.validateIdAndGet(id);
        return mapper.toDto(attendance);
    }

    @Override
    @Transactional
    public AttendanceDto create(AttendanceCreateDto createDto) {
        Lesson lesson = lessonValidator.validateIdAndGet(createDto.lessonId());

        Attendance attendance = new Attendance();
        attendance.setLesson(lesson);

        for (AttendanceStudentCreateDto item : createDto.students()) {
            Student student = studentValidator.validateIdAndGet(item.studentId());

            AttendanceStudent studentAttendance = new AttendanceStudent();
            studentAttendance.setStudent(student);
            studentAttendance.setStatus(item.status());
            studentAttendance.setReason(item.reason());
            attendance.addStudentAttendance(studentAttendance);
        }

        Attendance savedAttendance = repository.save(attendance);
        return mapper.toDto(savedAttendance);
    }

    @Override
    public AttendanceDto update(AttendanceUpdateDto updateDto, String id) {
        Attendance attendance = validator.validateIdAndGet(id);
        updateStudentAttendances(attendance, updateDto.attendanceStudents());
        Attendance save = repository.save(attendance);
        return mapper.toDto(save);
    }

    @Override
    public void delete(String id) {
        validator.validateId(id);
        repository.softDelete(id);
    }

    private void updateStudentAttendances(Attendance attendance, List<AttendanceStudentUpdateDto> attendanceStudentUpdateDtos) {
        // Convert existing students list to a map by student ID for O(1) lookup
        Map<String, AttendanceStudent> existingStudentsMap = attendance.getAttendanceStudents().stream()
                .collect(Collectors.toMap(
                        as -> as.getStudent().getId(),
                        as -> as
                ));

        for (AttendanceStudentUpdateDto dto : attendanceStudentUpdateDtos) {
            AttendanceStudent existingRecord = existingStudentsMap.get(dto.studentId());

            if (existingRecord != null) {
                // Update existing status
                existingRecord.setStatus(dto.status());
                existingRecord.setReason(dto.reason());
            } else {
                // Add new student record if not previously present
                Student student = studentValidator.validateIdAndGet(dto.studentId());
                AttendanceStudent newRecord = new AttendanceStudent();
                newRecord.setStudent(student);
                newRecord.setStatus(dto.status());
                newRecord.setReason(dto.reason());
                attendance.addStudentAttendance(newRecord);
            }
        }
    }

    public Integer getCount() {
        Optional<Integer> count = repository.getCount();
        return count.orElse(0);
    }

    public AttendanceDto getByStudentId(String studentId) {
        studentValidator.validateId(studentId);
        List<AttendanceProjection> byStudentId = repository.getByStudentId(studentId);
        return null;
    }

    public List<MonthlyAttendanceDto> getByGroup(String groupId, Integer previousMonths) {
        Integer intendedMonth = groupValidator.validateIdAndGetMonth(groupId, previousMonths);
        List<AttendanceProjection> allByGroupIdMonth = repository.findAllByGroupIdAndMonth(groupId, intendedMonth.toString(), previousMonths);
        if (allByGroupIdMonth.isEmpty()) {
            return List.of();
        }
        List<String> attIds = allByGroupIdMonth
                .stream()
                .map(AttendanceProjection::getId)
                .toList();

        List<AttendanceStudentProjection> studentAttendances = repository.findAttendanceStudentsByAttId(attIds);
        Map<String, Map<String, StatusReasonDto>> attendanceByAttId = studentAttendances.stream()
                .collect(
                        Collectors.groupingBy(AttendanceStudentProjection::getAttendanceId,
                                HashMap::new,
                                Collectors.toMap(
                                        AttendanceStudentProjection::getStudentId,
                                        a -> new StatusReasonDto(a.getStatus(), a.getReason()),
                                        (existing, replacement) -> replacement
                                ))
                );
        return allByGroupIdMonth.stream()
                .map(att -> new MonthlyAttendanceDto(
                        att.getId(),
                        att.getLessonTitle(),
                        att.getDate() != null ? att.getDate().toLocalDate() : null,
                        attendanceByAttId.getOrDefault(att.getId(), Collections.emptyMap())
                )).toList();
    }

    public List<MyAttendanceDto> getMyAttendance(String groupId, Integer previousMonths) {
        Integer intendedMonth = groupValidator.validateIdAndGetMonth(groupId, previousMonths);
        String userId = userValidator.authenticateAndGetId();
        List<MyAttendanceProjection> myAttendanceProjections = repository.getmyAttendance(groupId, userId, intendedMonth.toString());
        return mapper.toMyAttendanceDtoList(myAttendanceProjections);
    }
}
