package org.example.learningcenter.service;

import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.validation.annotation.Validated;

@Validated
public interface CrudService<CD extends Record, UD extends Record, D extends Record, I> {

    Page<D> getAll(Pageable pageable, String search);

    D get(I id);

    D create(@Valid CD createDto);

    D update(@Valid UD updateDto, I id);

    void delete(I id);
}
