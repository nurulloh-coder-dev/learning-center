package org.example.learningcenter.mapper;

import org.example.learningcenter.entity.annotation.IgnoreAuditFields;
import org.example.learningcenter.entity.dto.user.UserCreateDto;
import org.example.learningcenter.entity.dto.user.UserDto;
import org.example.learningcenter.entity.dto.user.UserUpdateDto;
import org.example.learningcenter.entity.model.User;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring")
public abstract class UserMapper {
    public abstract UserDto toDto(User user);

    @IgnoreAuditFields
    @Mapping(target = "branch", ignore = true)
    @Mapping(target = "organization", ignore = true)
    public abstract User toEntity(UserCreateDto createDto);

    @IgnoreAuditFields
    @Mapping(target = "role", ignore = true)
    @Mapping(target = "password", ignore = true)
    @Mapping(target = "birthDate", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    public abstract void mapUpdate(@MappingTarget User user, UserUpdateDto updateDto);
}