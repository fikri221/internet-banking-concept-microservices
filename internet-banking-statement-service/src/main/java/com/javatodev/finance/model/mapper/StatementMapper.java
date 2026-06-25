package com.javatodev.finance.model.mapper;

import com.javatodev.finance.model.dto.Statement;
import com.javatodev.finance.model.entity.StatementEntity;

import org.springframework.beans.BeanUtils;

public class StatementMapper extends BaseMapper<StatementEntity, Statement> {
    @Override
    public StatementEntity convertToEntity(Statement dto, Object... args) {
        StatementEntity entity = new StatementEntity();
        if (dto != null) {
            BeanUtils.copyProperties(dto, entity);
        }
        return entity;
    }

    @Override
    public Statement convertToDto(StatementEntity entity, Object... args) {
        Statement dto = new Statement();
        if (entity != null) {
            BeanUtils.copyProperties(entity, dto);
        }
        return dto;
    }
}
