package br.com.jurispay.api.controller.systemconfig;

import br.com.jurispay.api.dto.systemconfig.UpdateSystemConfigRequest;
import br.com.jurispay.application.systemconfig.dto.SystemConfigResponse;
import br.com.jurispay.application.systemconfig.dto.UpdateSystemConfigCommand;
import br.com.jurispay.application.systemconfig.usecase.DownloadSystemLogoUseCase;
import br.com.jurispay.application.systemconfig.usecase.GetSystemConfigUseCase;
import br.com.jurispay.application.systemconfig.usecase.UpdateSystemConfigUseCase;
import br.com.jurispay.application.systemconfig.usecase.UploadSystemLogoUseCase;
import jakarta.validation.Valid;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/system-config")
public class SystemConfigController {

    private final GetSystemConfigUseCase getSystemConfigUseCase;
    private final UpdateSystemConfigUseCase updateSystemConfigUseCase;
    private final UploadSystemLogoUseCase uploadSystemLogoUseCase;
    private final DownloadSystemLogoUseCase downloadSystemLogoUseCase;

    public SystemConfigController(
            GetSystemConfigUseCase getSystemConfigUseCase,
            UpdateSystemConfigUseCase updateSystemConfigUseCase,
            UploadSystemLogoUseCase uploadSystemLogoUseCase,
            DownloadSystemLogoUseCase downloadSystemLogoUseCase) {
        this.getSystemConfigUseCase = getSystemConfigUseCase;
        this.updateSystemConfigUseCase = updateSystemConfigUseCase;
        this.uploadSystemLogoUseCase = uploadSystemLogoUseCase;
        this.downloadSystemLogoUseCase = downloadSystemLogoUseCase;
    }

    @GetMapping
    public ResponseEntity<SystemConfigResponse> get() {
        return ResponseEntity.ok(getSystemConfigUseCase.get());
    }

    @PutMapping
    public ResponseEntity<SystemConfigResponse> update(@Valid @RequestBody UpdateSystemConfigRequest request) {
        var command = UpdateSystemConfigCommand.builder()
                .id(1L)
                .brandName(request.getBrandName())
                .contactEmail(request.getContactEmail())
                .contactPhone(request.getContactPhone())
                .cnpj(request.getCnpj())
                .build();

        return ResponseEntity.ok(updateSystemConfigUseCase.update(command));
    }

    @PostMapping(path = "/logo", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<SystemConfigResponse> uploadLogo(@RequestParam("file") MultipartFile file) throws IOException {
        return ResponseEntity.ok(uploadSystemLogoUseCase.upload(file.getBytes(), file.getOriginalFilename(), file.getContentType()));
    }

    @GetMapping("/logo")
    public ResponseEntity<byte[]> downloadLogo() {
        var logo = downloadSystemLogoUseCase.download();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType(logo.contentType()));
        headers.setContentDisposition(ContentDisposition.inline().filename(logo.originalFileName()).build());
        headers.setContentLength(logo.bytes().length);

        return ResponseEntity.ok().headers(headers).body(logo.bytes());
    }
}
