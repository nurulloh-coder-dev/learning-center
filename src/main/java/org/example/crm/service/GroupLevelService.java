package org.example.crm.service;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.example.crm.entity.dto.groupLevel.GroupLevelCreateDto;
import org.example.crm.entity.dto.groupLevel.GroupLevelDto;
import org.example.crm.entity.dto.groupLevel.GroupLevelNameDto;
import org.example.crm.entity.dto.groupLevel.GroupLevelUpdateDto;
import org.example.crm.entity.model.Level;
import org.example.crm.exceptions.ErrorCodes;
import org.example.crm.exceptions.ErrorType;
import org.example.crm.exceptions.RestException;
import org.example.crm.mapper.GroupLevelMapper;
import org.example.crm.projection.LevelNamesProjection;
import org.example.crm.repository.GroupLevelRepository;
import org.example.crm.validator.GroupLevelValidator;
import org.example.crm.validator.UserValidator;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class GroupLevelService extends AbstractService<
        GroupLevelRepository,
        GroupLevelMapper,
        GroupLevelValidator> implements CrudService<GroupLevelCreateDto, GroupLevelUpdateDto, GroupLevelDto, String> {


    private final UserValidator userValidator;

    protected GroupLevelService(GroupLevelRepository repository, GroupLevelMapper mapper, GroupLevelValidator validator, UserValidator userValidator) {
        super(repository, mapper, validator);
        this.userValidator = userValidator;
    }

    public List<GroupLevelDto> getGroupLevels(String search) {
        String organizationId = userValidator.authenticateAndGetOrganizationId();
        List<Level> level = repository.findAllLevelsByOrganizationIdAndSearch(organizationId, search);
        return mapper.toListDto(level);
    }

    @Override
    public Page<GroupLevelDto> getAll(Pageable pageable, String search) {
        return null;
    }

    @Override
    public GroupLevelDto get(String id) {
        String organizationId = userValidator.authenticateAndGetOrganizationId();
        validator.validateAndGet(id);
        Level level = repository.findLevelByIdAndOrganizationId(id, organizationId)
                .orElseThrow(() -> new RestException(ErrorType.GROUP_LEVEL_NOT_FOUND, ErrorCodes.NotFound));
        return mapper.toDto(level);
    }

    @Override
    public GroupLevelDto create(GroupLevelCreateDto createDto) {
        validator.validateForCreate(createDto);
        Level level = mapper.toEntity(createDto);
        Level savedLevel = repository.save(level);
        return mapper.toDto(savedLevel);
    }

    @Override
    public GroupLevelDto update(GroupLevelUpdateDto updateDto, String id) {
        return null;
    }

    @Override
    @Transactional
    public void delete(String id) {
        Level level = validator.validateAndGet(id);
        repository.updateLevelDeleted(level.getId());
    }

    @Transactional
    public List<GroupLevelDto> update(List<GroupLevelUpdateDto> levels) {
        List<Level> updatedLevels = levels.stream().map(levelUpdateDto -> {
            Level level = repository.findById(levelUpdateDto.id())
                    .orElseThrow(() -> new RestException(ErrorType.GROUP_LEVEL_NOT_FOUND, ErrorCodes.NotFound));
            level.setOrderNumber(levelUpdateDto.orderNumber());
            return repository.save(level);
        }).toList();
        return mapper.toListDto(updatedLevels);
    }


    public List<GroupLevelNameDto> getGroupLevelsName() {
        String organizationId = userValidator.authenticateAndGetOrganizationId();
        log.info("orgId->{}", organizationId);
        List<LevelNamesProjection> levelNames = repository.getLevelNames(organizationId);
        log.info("size of result is {}",levelNames.size());
        return levelNames.stream()
                .map(l -> new GroupLevelNameDto(l.getId(), l.getName()))
                .toList();
    }
}
