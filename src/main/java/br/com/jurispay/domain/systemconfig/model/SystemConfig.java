package br.com.jurispay.domain.systemconfig.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SystemConfig {

    private Long id;

    private String brandName;
    private String contactEmail;
    private String contactPhone;
    private String cnpj;

    private String logoOriginalFileName;
    private String logoContentType;
    private Long logoSizeBytes;
    private String logoBucket;
    private String logoObjectKey;

    private Instant createdAt;
    private Instant updatedAt;
}
