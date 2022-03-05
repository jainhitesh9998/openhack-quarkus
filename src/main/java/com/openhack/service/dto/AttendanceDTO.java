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
    private String deviceIdentifier;
    private Instant createdAt;
    private Long identifier;
    private List<Double> embeddings;
    private Boolean faceIdentified;
    private Double temperature;
    private Double score;
}
