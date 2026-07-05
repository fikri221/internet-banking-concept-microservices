package com.javatodev.finance.model.mapper;

import com.javatodev.finance.model.dto.response.StatementResponse;
import com.javatodev.finance.model.entity.StatementEntity;

import org.springframework.beans.BeanUtils;

public class StatementMapper extends BaseMapper<StatementEntity, StatementResponse> {
    @Override
    public StatementEntity convertToEntity(StatementResponse dto, Object... args) {
        StatementEntity entity = new StatementEntity();
        if (dto != null) {
            BeanUtils.copyProperties(dto, entity);
        }
        return entity;
    }

    @Override
    public StatementResponse convertToDto(StatementEntity entity, Object... args) {
        StatementResponse dto = new StatementResponse();
        if (entity != null) {
            BeanUtils.copyProperties(entity, dto);
        }
        return dto;
    }
}
