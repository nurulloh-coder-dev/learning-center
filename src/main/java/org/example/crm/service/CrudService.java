package org.example.crm.service;

import jakarta.validation.Valid;
import org.example.crm.filters.SearchFilterDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.validation.annotation.Validated;

@Validated
public interface CrudService<FD extends Record,CD extends Record, UD extends Record, D extends Record, I, R> {

    R getAll(Pageable pageable, FD filterDto);

    D get(I id);

    D create(@Valid CD createDto);

    D update(@Valid UD updateDto, I id);

    void delete(I id);
}
