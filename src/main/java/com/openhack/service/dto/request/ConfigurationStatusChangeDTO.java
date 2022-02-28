package com.openhack.service.dto.request;

import io.quarkus.runtime.annotations.RegisterForReflection;
import lombok.Data;

@Data
@RegisterForReflection
public class ConfigurationStatusChangeDTO {
    private String deviceIdentifier;
    private Boolean status;
}
