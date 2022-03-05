package com.openhack.config.serde;

import com.openhack.service.dto.AttendanceDTO;
import io.quarkus.kafka.client.serialization.ObjectMapperDeserializer;

public class AttendanceDTODeserializer extends ObjectMapperDeserializer<AttendanceDTO> {
    public AttendanceDTODeserializer(){
        super(AttendanceDTO.class);
    }
}
