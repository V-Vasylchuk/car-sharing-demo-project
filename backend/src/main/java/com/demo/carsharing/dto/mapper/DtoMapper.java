package com.demo.carsharing.dto.mapper;

public interface DtoMapper<D, T, S> {
    D toModel(T requestDto);

    D toModel(T requestDto, D model);

    S toDto(D model);
}
