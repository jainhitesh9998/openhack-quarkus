package com.openhack.domain;

import io.quarkiverse.hibernate.types.json.JsonBinaryType;
import io.quarkiverse.hibernate.types.json.JsonType;
import io.quarkiverse.hibernate.types.json.JsonTypes;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import javax.persistence.*;
import java.time.Instant;
import java.util.List;

@Setter
@Getter
@Entity(name = "Attendance")
@Table(name = "attendance")
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@TypeDef(name = JsonTypes.JSON, typeClass = JsonType.class)
@TypeDef(name = JsonTypes.JSON_BIN, typeClass = JsonBinaryType.class)
public class Attendance {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "authenticated")
    private Boolean authenticated;

    @Column(name = "mac_address")
    private String macAddress;

    @Column(name = "created_at")
    private Instant createdAt;

    @Column(name = "identifier")
    private Long identifier;

    @Type(type = JsonTypes.JSON_BIN)
    @Column(name = "embedding", columnDefinition = JsonTypes.JSON_BIN)
    private List<Double> embeddings;

    @Column(name = "face_detected")
    private Boolean faceDetected;

    @Column(name = "temperature")
    private Double temperature;

    @Column(name = "score")
    private Long score;


}
