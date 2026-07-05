package com.javatodev.finance.model.mapper;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.*;

public abstract class BaseMapper<E, D> {
    public abstract E convertToEntity(D dto, Object... args);

    public abstract D convertToDto(E entity, Object... args);

    public Collection<E> convertToEntity(Collection<D> dto, Object... args) {
        return dto.stream().map(d -> convertToEntity(d, args)).toList();
    }

    public Collection<D> convertToDto(Collection<E> entity, Object... args) {
        return entity.stream().map(e -> convertToDto(e, args)).toList();
    }

    public List<E> convertToEntityList(Collection<D> dto, Object... args) {
        return new ArrayList<>(convertToEntity(dto, args));
    }

    public List<D> convertToDtoList(Collection<E> entity, Object... args) {
        return new ArrayList<>(convertToDto(entity, args));
    }

    public Page<D> convertToDtoPage(Page<E> entity, Pageable pageable, Object... args) {
        return new PageImpl<>(convertToDtoList(entity.getContent(), args), pageable, entity.getTotalElements());
    }

    public Set<E> convertToEntitySet(Collection<D> dto, Object... args) {
        return new HashSet<>(convertToEntity(dto, args));
    }

    public Set<D> convertToDtoSet(Collection<E> entity, Object... args) {
        return new HashSet<>(convertToDto(entity, args));
    }
}
