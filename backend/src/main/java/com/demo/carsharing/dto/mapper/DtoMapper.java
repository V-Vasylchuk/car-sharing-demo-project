package com.demo.carsharing.dto.mapper;

public interface DtoMapper<D, T, S> {
    D toModel(T requestDto);

    S toDto(D model);
}
