package org.example.crm.mapper;

import org.example.crm.entity.dto.user.UserDto;
import org.example.crm.entity.model.UserOrganization;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserOrganizationMapper {

    @Mapping(target = "id", source = "user.id")
    @Mapping(target = "branchId", source = "branch.id")
    @Mapping(target = "imageUrl", source = "user.imageUrl")
    @Mapping(target = "fullName", source = "user.fullName")
    @Mapping(target = "phone", source = "user.phone")
    @Mapping(target = "birthDate", source = "user.birthDate")
    @Mapping(target = "role", source = "role")
    UserDto toUserDto(UserOrganization userOrganization);
}