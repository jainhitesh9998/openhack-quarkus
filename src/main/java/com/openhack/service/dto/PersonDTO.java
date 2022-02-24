package com.openhack.service.dto;

import io.quarkus.runtime.annotations.RegisterForReflection;
import org.hibernate.engine.spi.Status;

import java.time.Instant;

@RegisterForReflection
public class PersonDTO {
    private Long id;
    private String name;
    private Instant birth;
    private Status status;
    private String country;
    private String city;

}
