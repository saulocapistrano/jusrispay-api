package br.com.jurispay.domain.document.repository;

import br.com.jurispay.domain.document.model.PutFileCommand;
import br.com.jurispay.domain.document.model.StoredFile;

import java.util.Optional;

/**
 * Porta de repositório para armazenamento de arquivos (MinIO/S3).
 * Abstrai a implementação específica do storage.
 */
public interface FileStorageRepository {

    /**
     * Faz upload de um arquivo no storage.
     *
     * @param command comando com informações do arquivo a ser armazenado
     * @return informações do arquivo armazenado (metadados)
     */
    StoredFile put(PutFileCommand command);

    /**
     * Busca metadados de um arquivo no storage.
     * Não retorna o conteúdo binário, apenas informações sobre o arquivo.
     *
     * @param bucket nome do bucket
     * @param objectKey chave/KEY do objeto
     * @return metadados do arquivo ou Optional.empty() se não encontrado
     */
    Optional<StoredFile> get(String bucket, String objectKey);

    /**
     * Remove um arquivo do storage.
     *
     * @param bucket nome do bucket
     * @param objectKey chave/KEY do objeto
     */
    void delete(String bucket, String objectKey);
}

