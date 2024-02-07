package org.example.servlet.mapper;

public interface DtoMapper<K, T> {
    T toDto(K book);

    K toEntity(T bookDto);
}
