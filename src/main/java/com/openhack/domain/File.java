package com.openhack.domain;


import com.openhack.domain.enumeration.EncryptionAlgorithm;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;


@Entity(name = "File")
@Table(name = "file")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Setter
@Getter
@EqualsAndHashCode
@ToString
public class File {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "identifier")
    private String identifier;

    @Column(name = "location")
    private String location;

    @Column(name = "uri")
    private String uri;

    @Column(name = "encryption")
    private Boolean encryption;

    @Column(name = "encryption_algorithm")
    @Enumerated(value = EnumType.STRING)
    private EncryptionAlgorithm encryptionAlgorithm;

    @ManyToOne
    @JoinColumn(name = "employee_id")
    private Employee employee;


}
