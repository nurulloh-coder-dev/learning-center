package org.example.crm.mapper;

import org.example.crm.entity.dto.subscription.SubscriptionDto;
import org.example.crm.entity.model.Subscription;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface SubscriptionMapper {

    SubscriptionDto toDto(Subscription subscription);

    // no toEntity / mapUpdate — activation and status changes go through
    // service methods with real logic, not a field-by-field copy
}