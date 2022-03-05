package com.openhack.service.dto;

import io.quarkus.runtime.annotations.RegisterForReflection;
import lombok.Data;

import java.io.Serializable;

@Data
@RegisterForReflection
public class AnalyticsDTO implements Serializable {
    private Long activeDevices;
    private Long inactiveDevices;
    private Long totalDevices;
    private Long disabledDevices;
    private Long totalAuth;
    private Long totalAuthInDay;
    private Long uniqueAuthInDay;
    private Long activeUsers;
    private Long disabledUsers;
}
