package com.openhack.domain;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.engine.spi.Status;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.time.Instant;

@Setter
@Getter
@Entity
public class Person {
    @Id
    @GeneratedValue
    private Long id;
    private String name;
    private Instant birth;
    private Status status;
    private String country;
    private String city;
}