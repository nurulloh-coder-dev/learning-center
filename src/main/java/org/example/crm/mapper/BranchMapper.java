package org.example.crm.mapper;


import org.example.crm.entity.dto.branch.BranchCreateDto;
import org.example.crm.entity.dto.branch.BranchDto;
import org.example.crm.entity.dto.branch.BranchUpdateDto;
import org.example.crm.entity.model.Branch;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(
        componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface BranchMapper {


    BranchDto toDto(Branch branch);


    @Mapping(target = "chargeForMonth", source = "createDto.chargeForMonth")
    @Mapping(target = "name", source = "createDto.name")
    @Mapping(target = "address", source = "createDto.address")
    @Mapping(target = "googlePlaceId", source = "createDto.googlePlaceId")
    @Mapping(target = "latitude", source = "createDto.latitude")
    @Mapping(target = "longitude", source = "createDto.longitude")
    @Mapping(target = "googleMapsUrl", source = "createDto.googleMapsUrl")
    Branch toEntity(BranchCreateDto createDto);


    void updateEntity(@MappingTarget Branch branch, BranchUpdateDto updateDto);
}
