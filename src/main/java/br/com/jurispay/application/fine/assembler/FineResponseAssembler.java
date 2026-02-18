package br.com.jurispay.application.fine.assembler;

import br.com.jurispay.api.dto.fine.FineResponse;
import br.com.jurispay.domain.fine.model.Fine;
import org.springframework.stereotype.Component;

@Component
public class FineResponseAssembler {

    public FineResponse toResponse(Fine fine) {
        if (fine == null) {
            return null;
        }

        return FineResponse.builder()
                .id(fine.getId())
                .nome(fine.getNome())
                .valor(fine.getValor())
                .ativo(fine.getAtivo())
                .dataCriacao(fine.getDataCriacao())
                .dataAtualizacao(fine.getDataAtualizacao())
                .build();
    }
}
