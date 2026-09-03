package org.example.crm.entity.request;

import org.example.crm.entity.dto.groupLevel.GroupLevelOrderUpdateDto;
import org.example.crm.entity.dto.groupLevel.GroupLevelUpdateDto;

import java.util.List;

public record GroupLevelList(
        List<GroupLevelOrderUpdateDto> levels
) {
}
