package br.com.jurispay.application.systemconfig.usecase;

public interface DownloadSystemLogoUseCase {

    DownloadedLogo download();

    record DownloadedLogo(byte[] bytes, String contentType, String originalFileName) {}
}
