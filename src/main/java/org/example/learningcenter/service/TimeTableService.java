package org.example.learningcenter.service;

import jakarta.transaction.Transactional;
import org.example.learningcenter.entity.dto.TimeTableCreateDto;
import org.example.learningcenter.entity.dto.TimeTableUpdateDto;
import org.example.learningcenter.entity.dto.timeTable.TimeTableDto;
import org.example.learningcenter.entity.enums.DayType;
import org.example.learningcenter.entity.model.TimeTable;
import org.example.learningcenter.mapper.TimeTableMapper;
import org.example.learningcenter.projection.TimeTableProjection;
import org.example.learningcenter.repository.TimeTableRepository;
import org.example.learningcenter.validator.TimeTableValidator;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.util.List;

@Service
public class TimeTableService extends AbstractService<
        TimeTableRepository,
        TimeTableMapper,
        TimeTableValidator> implements CrudService<TimeTableCreateDto, TimeTableUpdateDto, TimeTableDto, String> {

    protected TimeTableService(TimeTableRepository repository, TimeTableMapper mapper, TimeTableValidator validator) {
        super(repository, mapper, validator);
    }

    @Override
    public Page<TimeTableDto> getAll(Pageable pageable, String search) {
        return null;
    }

    @Override
    public TimeTableDto get(String id) {
        TimeTable entity = validator.validateAndGet(id);
        return mapper.toDto(entity);

    }

    @Override
    public TimeTableDto create(TimeTableCreateDto createDto) {
        validator.validate(createDto);
        TimeTable entity = mapper.toEntity(createDto);
        return mapper.toDto(repository.save(entity));
    }

    @Override
    @Transactional
    public TimeTableDto update(TimeTableUpdateDto updateDto, String id) {
        TimeTable timeTable = validator.validateAndGet(id);
        mapper.update(timeTable, updateDto);
        return mapper.toDto(repository.save(timeTable));
    }

    @Override
    @Transactional
    public void delete(String id) {
        TimeTable timeTable = validator.validateAndGet(id);
        repository.updateDeleted(timeTable.getId());
    }

    public List<TimeTableDto> getAll(DayType dayType, LocalTime start, LocalTime end) {
        List<TimeTableProjection> projection = repository.getAllTimeTableByFilter(dayType,start,end);
        return projection.stream()
                .map(mapper::toDtoFromProjection)
                .toList();
    }
}
