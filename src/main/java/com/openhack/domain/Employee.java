package com.openhack.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.List;

@Setter
@Getter
@Entity(name = "Employee")
@Table(name = "employee")
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Employee {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "identifier", unique = true)
    private String identifier;

    @Column(name = "name")
    private String name;

    @Column(name = "encryption_key")
    private String encryptionKey;

    @Column(name = "enabled")
    private Boolean enabled;

    @Column(name = "created_at")
    private Instant createdAt;

    @Column(name = "deletion")
    private Boolean deletion;

    @OneToMany(mappedBy = "employee", cascade = CascadeType.ALL)
    private List<File> files;

    @OneToMany(mappedBy = "employee", cascade = CascadeType.ALL)
//    @JsonIgnoreProperties(value = {"employee"}, allowGetters = true)
    private List<Embeddings> embeddings;

}
