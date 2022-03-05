package com.openhack.service.messaging;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.openhack.service.ConfigurationService;
import com.openhack.service.dto.request.ConfigurationStatusChangeDTO;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.slf4j.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;

@ApplicationScoped
public class ConfigurationConsumer {

    @Inject
    Logger LOG;
    @Inject
    ConfigurationService configurationService;

    @Transactional
    @Incoming("topic-status")
    public void process(byte[] raw){
        String data = new String(raw);
        try {
            ConfigurationStatusChangeDTO configurationStatusChangeDTO = new ObjectMapper().readValue(data, ConfigurationStatusChangeDTO.class);
            LOG.info("{} ", configurationStatusChangeDTO);
            configurationService.updateConfigStatus(configurationStatusChangeDTO.getDeviceIdentifier(), configurationStatusChangeDTO.getStatus());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }



}
