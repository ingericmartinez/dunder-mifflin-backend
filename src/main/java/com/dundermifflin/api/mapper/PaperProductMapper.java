package com.dundermifflin.api.mapper;

import com.dundermifflin.api.domain.PaperProduct;
import com.dundermifflin.api.dto.PaperProductDto;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface PaperProductMapper {
    PaperProductDto toDto(PaperProduct entity);
    PaperProduct toEntity(PaperProductDto dto);
}
