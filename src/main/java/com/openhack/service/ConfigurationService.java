package com.openhack.service;

import com.openhack.service.dto.AnalyticsDTO;
import com.openhack.service.dto.ConfigurationDTO;

import java.util.List;
import java.util.Optional;

public interface ConfigurationService {
    ConfigurationDTO save(ConfigurationDTO configurationDTO);
    ConfigurationDTO update(ConfigurationDTO configurationDTO);
    List<ConfigurationDTO> getAll();
    Optional<ConfigurationDTO> getConfigByIdentifier(String identifier);
    void updateConfigStatus(String deviceIdentifier, Boolean status);
    AnalyticsDTO getAnalytics();
    void delete(Long id);
}
