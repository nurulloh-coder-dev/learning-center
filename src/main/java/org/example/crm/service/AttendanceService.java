package org.example.crm.service;

import org.example.crm.entity.dto.attendance.AttendanceCreateDto;
import org.example.crm.entity.dto.attendance.AttendanceDto;
import org.example.crm.entity.dto.attendance.AttendanceUpdateDto;
import org.example.crm.entity.dto.attendance.MonthlyAttendanceDto;
import org.example.crm.entity.dto.attendanceStudent.AttendanceStudentCreateDto;
import org.example.crm.entity.dto.attendanceStudent.AttendanceStudentDto;
import org.example.crm.entity.dto.attendanceStudent.AttendanceStudentUpdateDto;
import org.example.crm.entity.model.Attendance;
import org.example.crm.entity.model.AttendanceStudent;
import org.example.crm.entity.model.Lesson;
import org.example.crm.entity.model.Student;
import org.example.crm.mapper.AttendanceMapper;
import org.example.crm.projection.AttendanceProjection;
import org.example.crm.projection.AttendanceStudentProjection;
import org.example.crm.repository.AttendanceRepository;
import org.example.crm.validator.AttendanceValidator;
import org.example.crm.validator.LessonValidator;
import org.example.crm.validator.StudentValidator;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AttendanceService extends AbstractService<
        AttendanceRepository,
        AttendanceMapper,
        AttendanceValidator> implements CrudService<AttendanceCreateDto, AttendanceUpdateDto, AttendanceDto, String> {

    private final LessonValidator lessonValidator;
    private final StudentValidator studentValidator;

    protected AttendanceService(AttendanceRepository repository, AttendanceMapper mapper, AttendanceValidator validator, LessonValidator lessonValidator, StudentValidator studentValidator) {
        super(repository, mapper, validator);
        this.lessonValidator = lessonValidator;
        this.studentValidator = studentValidator;
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
            } else {
                // Add new student record if not previously present
                Student student = studentValidator.validateIdAndGet(dto.studentId());
                AttendanceStudent newRecord = new AttendanceStudent();
                newRecord.setStudent(student);
                newRecord.setStatus(dto.status());
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
        List<AttendanceProjection> allByGroupIdThisMonth = repository.findAllByGroupIdAndMonth(groupId,previousMonths);
        if (allByGroupIdThisMonth.isEmpty()) {
            return List.of();
        }
        List<String> attIds = allByGroupIdThisMonth
                .stream()
                .map(AttendanceProjection::getId)
                .toList();

        List<AttendanceStudentProjection> attendanceStudentsByAttId = repository.findAttendanceStudentsByAttId(attIds);
        Map<String, List<AttendanceStudentDto>> collect = attendanceStudentsByAttId
                .stream()
                .collect(
                        Collectors.groupingBy(AttendanceStudentProjection::getAttendanceId,
                                Collectors.mapping(p -> new AttendanceStudentDto(
                                        p.getStudentId(),
                                        p.getStudentFullName(),
                                        p.getStudentImageUrl(),
                                        p.getStatus()
                                ), Collectors.toList())
                        ));
        return allByGroupIdThisMonth.stream()
                .map(att -> new MonthlyAttendanceDto(
                        att.getId(),
                        att.getLessonTitle(),
                        att.getDate() != null ? att.getDate().toLocalDate() : null,
                        collect.getOrDefault(att.getId(), List.of())
                )).toList();
    }
}
