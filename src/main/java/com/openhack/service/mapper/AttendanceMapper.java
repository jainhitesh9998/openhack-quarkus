package com.openhack.service.mapper;

import com.openhack.domain.Attendance;
import com.openhack.service.dto.AttendanceDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "cdi")
public interface AttendanceMapper extends EntityMapper<AttendanceDTO, Attendance> {
}
