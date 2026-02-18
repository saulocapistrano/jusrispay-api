package br.com.jurispay.application.fine.usecase;

import br.com.jurispay.api.dto.fine.FineRequest;
import br.com.jurispay.api.dto.fine.FineResponse;

public interface CreateFineUseCase {
    FineResponse create(FineRequest request);
}
