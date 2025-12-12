package br.com.jurispay.domain.document.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Representa um arquivo armazenado no storage (MinIO/S3).
 * Contém apenas metadados, não o conteúdo binário.
 */
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StoredFile {

    /**
     * Nome do bucket onde o arquivo está armazenado.
     */
    private String bucket;

    /**
     * Chave/KEY do objeto no storage.
     */
    private String objectKey;

    /**
     * Content-Type do arquivo.
     */
    private String contentType;

    /**
     * Nome original do arquivo.
     */
    private String originalFileName;

    /**
     * Tamanho do arquivo em bytes.
     */
    private Long sizeBytes;
}

