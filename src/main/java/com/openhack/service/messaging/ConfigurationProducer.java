package com.openhack.service.messaging;

import com.openhack.service.dto.request.ConfigurationChangeDTO;
import io.smallrye.mutiny.Multi;
import io.smallrye.reactive.messaging.mqtt.MqttMessage;
import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.eclipse.microprofile.reactive.messaging.Outgoing;
import org.slf4j.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

@ApplicationScoped
public class ConfigurationProducer {
    @Inject
    Logger LOG;
    @Inject @Channel("change-configuration")
    Emitter<ConfigurationChangeDTO> configurationChangeDTOEmitter;


    public void send(ConfigurationChangeDTO configurationChangeDTO){
        configurationChangeDTOEmitter.send(configurationChangeDTO).whenComplete((success, failure) -> {
            if(failure != null){
                LOG.info("error : {}", failure.getMessage());
            } else {
                LOG.info("message sent successfully");
            }
        });
    }

    @Incoming("change-configuration")
    @Outgoing("topic-configuration")
    public MqttMessage<ConfigurationChangeDTO> publish(ConfigurationChangeDTO configurationChangeDTO){
        LOG.info("Message sent to the topic");
        return MqttMessage.of(configurationChangeDTO);
    }

}
