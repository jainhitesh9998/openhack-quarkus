package com.openhack.service.mapper;

import com.openhack.domain.File;
import com.openhack.service.dto.FileDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "cdi", uses = {EmployeeMapper.class})
public interface FilesMapper extends EntityMapper<FileDTO, File>{
    @Mapping(source = "employee", target = "employee", qualifiedByName = "id")
    FileDTO toDto(File file);

    @Mapping(source = "employee", target = "employee", qualifiedByName = "id")
    File toEntity(FileDTO fileDTO);
}

