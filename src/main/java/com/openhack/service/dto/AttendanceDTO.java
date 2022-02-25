package com.openhack.service.dto;

import io.quarkus.runtime.annotations.RegisterForReflection;
import lombok.Data;

import javax.persistence.Column;
import java.io.Serializable;
import java.time.Instant;
import java.util.List;

@Data
@RegisterForReflection
public class AttendanceDTO implements Serializable {
    private Long id;
    private Boolean authenticated;
    private String macAddress;
    private Instant createdAt;
    private String identifier;
    private List<Double> embeddings;
    private Boolean faceDetected;
    private Double temperature;
}
