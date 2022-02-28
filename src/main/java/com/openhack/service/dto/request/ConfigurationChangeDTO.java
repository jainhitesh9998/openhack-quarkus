package com.openhack.service.dto.request;

import io.quarkus.runtime.annotations.RegisterForReflection;
import lombok.Data;

@Data
@RegisterForReflection
public class ConfigurationChangeDTO {
    private String deviceIdentifier;
    private Boolean enabled;
}
