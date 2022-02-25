package com.openhack.service.mapper;

import com.openhack.domain.Embeddings;
import com.openhack.domain.File;
import com.openhack.service.dto.EmbeddingDTO;
import com.openhack.service.dto.FileDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "cdi", uses = {EmployeeMapper.class})
public interface EmbeddingMapper extends EntityMapper<EmbeddingDTO, Embeddings>{
    @Mapping(source = "employee", target = "employee", qualifiedByName = "id")
    EmbeddingDTO toDto(Embeddings embeddings);

    @Mapping(source = "employee", target = "employee", qualifiedByName = "id")
    Embeddings toEntity(EmbeddingDTO embeddingDTO);
}

