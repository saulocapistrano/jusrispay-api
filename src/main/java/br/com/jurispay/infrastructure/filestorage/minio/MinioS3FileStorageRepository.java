package br.com.jurispay.infrastructure.filestorage.minio;

import br.com.jurispay.domain.document.model.PutFileCommand;
import br.com.jurispay.domain.document.model.StoredFile;
import br.com.jurispay.domain.document.repository.FileStorageRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;

import java.net.URI;
import java.util.Optional;

/**
 * Implementação do FileStorageRepository usando AWS SDK v2 para MinIO/S3.
 */
@Component
public class MinioS3FileStorageRepository implements FileStorageRepository {

    @Value("${jurispay.filestorage.endpoint}")
    private String endpoint;

    @Value("${jurispay.filestorage.access-key}")
    private String accessKey;

    @Value("${jurispay.filestorage.secret-key}")
    private String secretKey;

    @Value("${jurispay.filestorage.region:us-east-1}")
    private String region;

    private S3Client s3Client;

    @PostConstruct
    public void init() {
        AwsBasicCredentials credentials = AwsBasicCredentials.create(accessKey, secretKey);

        s3Client = S3Client.builder()
                .endpointOverride(URI.create(endpoint))
                .credentialsProvider(StaticCredentialsProvider.create(credentials))
                .region(Region.of(region))
                .forcePathStyle(true) // Necessário para MinIO
                .build();
    }

    @Override
    public StoredFile put(PutFileCommand command) {
        try {
            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                    .bucket(command.getBucket())
                    .key(command.getObjectKey())
                    .contentType(command.getContentType())
                    .build();

            RequestBody requestBody = RequestBody.fromBytes(command.getBytes());

            s3Client.putObject(putObjectRequest, requestBody);

            return StoredFile.builder()
                    .bucket(command.getBucket())
                    .objectKey(command.getObjectKey())
                    .contentType(command.getContentType())
                    .originalFileName(command.getOriginalFileName())
                    .sizeBytes((long) command.getBytes().length)
                    .build();
        } catch (Exception e) {
            throw new RuntimeException(
                    "Erro ao fazer upload do arquivo (bucket=" + command.getBucket() + ", objectKey=" + command.getObjectKey() + "): " + e.getMessage(),
                    e
            );
        }
    }

    @Override
    public Optional<StoredFile> get(String bucket, String objectKey) {
        try {
            HeadObjectRequest headRequest = HeadObjectRequest.builder()
                    .bucket(bucket)
                    .key(objectKey)
                    .build();

            HeadObjectResponse response = s3Client.headObject(headRequest);

            return Optional.of(StoredFile.builder()
                    .bucket(bucket)
                    .objectKey(objectKey)
                    .contentType(response.contentType())
                    .sizeBytes(response.contentLength())
                    .build());
        } catch (NoSuchKeyException e) {
            return Optional.empty();
        } catch (Exception e) {
            throw new RuntimeException("Erro ao buscar arquivo: " + e.getMessage(), e);
        }
    }

    @Override
    public void delete(String bucket, String objectKey) {
        try {
            DeleteObjectRequest deleteRequest = DeleteObjectRequest.builder()
                    .bucket(bucket)
                    .key(objectKey)
                    .build();

            s3Client.deleteObject(deleteRequest);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao deletar arquivo: " + e.getMessage(), e);
        }
    }

    @Override
    public byte[] getBytes(String bucket, String objectKey) {
        try {
            GetObjectRequest getRequest = GetObjectRequest.builder()
                    .bucket(bucket)
                    .key(objectKey)
                    .build();

            return s3Client.getObjectAsBytes(getRequest).asByteArray();
        } catch (NoSuchKeyException e) {
            throw new RuntimeException("Arquivo não encontrado: " + objectKey, e);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao baixar arquivo: " + e.getMessage(), e);
        }
    }
}

