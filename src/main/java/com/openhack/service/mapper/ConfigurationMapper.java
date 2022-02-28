package com.openhack.service.mapper;

import com.openhack.domain.Attendance;
import com.openhack.domain.Configuration;
import com.openhack.service.dto.AttendanceDTO;
import com.openhack.service.dto.ConfigurationDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "cdi")
public interface ConfigurationMapper extends EntityMapper<ConfigurationDTO, Configuration> {
}
