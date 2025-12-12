package br.com.jurispay.domain.document.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Comando para fazer upload de um arquivo no storage (MinIO/S3).
 */
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PutFileCommand {

    /**
     * Nome do bucket onde o arquivo será armazenado.
     */
    private String bucket;

    /**
     * Chave/KEY do objeto no storage (caminho completo do arquivo).
     */
    private String objectKey;

    /**
     * Content-Type do arquivo (ex: application/pdf, image/jpeg).
     */
    private String contentType;

    /**
     * Nome original do arquivo enviado pelo usuário.
     */
    private String originalFileName;

    /**
     * Conteúdo binário do arquivo.
     */
    private byte[] bytes;
}

