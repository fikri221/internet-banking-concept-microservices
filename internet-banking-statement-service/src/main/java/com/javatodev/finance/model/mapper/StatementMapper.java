package com.javatodev.finance.model.mapper;

import com.javatodev.finance.model.dto.response.StatementResponse;
import com.javatodev.finance.model.entity.StatementEntity;

import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public abstract class StatementMapper extends BaseMapper<StatementEntity, StatementResponse> {
    public abstract StatementEntity toEntity(StatementResponse dto);
    public abstract StatementResponse toDto(StatementEntity entity);

    @Override
    public StatementEntity convertToEntity(StatementResponse dto, Object... args) {
        return toEntity(dto);
    }

    @Override
    public StatementResponse convertToDto(StatementEntity entity, Object... args) {
        return toDto(entity);
    }
}
