package com.openhack.service.dto;

import io.quarkus.runtime.annotations.RegisterForReflection;
import lombok.Data;

import javax.persistence.Column;
import java.io.Serializable;
import java.time.Instant;

@RegisterForReflection
@Data
public class ConfigurationDTO implements Serializable {
    private Long id;

    private String deviceName;

    private String deviceIdentifier;

    private String deviceType;

    private Boolean enabled;

    private Boolean status;

    private String location;

    private Instant createdAt;

    private Instant updatedAt;
}
