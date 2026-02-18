package br.com.jurispay.application.systemconfig.usecase;

import br.com.jurispay.application.systemconfig.dto.SystemConfigResponse;

public interface UploadSystemLogoUseCase {

    SystemConfigResponse upload(byte[] bytes, String originalFileName, String contentType);
}
