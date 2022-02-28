package com.openhack.domain;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

@Setter
@Getter
@Entity(name = "Configuration")
@Table(name = "configuration")
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Configuration {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "device_name")
    private String deviceName;

    @Column(name = "device_identifier", unique = true)
    private String deviceIdentifier;

    @Column(name = "device_type")
    private String deviceType;

    @Column(name = "enabled")
    private Boolean enabled;

    @Column(name = "status")
    private Boolean status;

    @Column(name = "location")
    private Boolean location;

}

