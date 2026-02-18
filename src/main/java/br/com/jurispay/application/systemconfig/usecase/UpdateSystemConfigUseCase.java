package br.com.jurispay.application.systemconfig.usecase;

import br.com.jurispay.application.systemconfig.dto.SystemConfigResponse;
import br.com.jurispay.application.systemconfig.dto.UpdateSystemConfigCommand;

public interface UpdateSystemConfigUseCase {

    SystemConfigResponse update(UpdateSystemConfigCommand command);
}
