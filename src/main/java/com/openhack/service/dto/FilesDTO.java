package com.openhack.service.dto;

import com.openhack.domain.enumeration.EncryptionAlgorithm;

public class FilesDTO {
    private Long id;
    private String identifier;
    private String uri;
    private Boolean encryption;
    private EncryptionAlgorithm encryptionAlgorithm;

}
