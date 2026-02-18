package br.com.jurispay.application.fine.usecase;

import br.com.jurispay.api.dto.fine.FineResponse;

import java.util.List;

public interface ListFinesUseCase {
    List<FineResponse> listAll();
}
