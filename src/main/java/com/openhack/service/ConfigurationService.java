package com.openhack.service;

import com.openhack.service.dto.ConfigurationDTO;

import java.util.List;

public interface ConfigurationService {
    ConfigurationDTO save(ConfigurationDTO configurationDTO);
    ConfigurationDTO update(ConfigurationDTO configurationDTO);
    List<ConfigurationDTO> getAll();
}
