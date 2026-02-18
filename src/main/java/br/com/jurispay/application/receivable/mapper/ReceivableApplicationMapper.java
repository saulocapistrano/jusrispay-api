package br.com.jurispay.application.receivable.mapper;

import br.com.jurispay.application.receivable.dto.ReceivableResponse;
import br.com.jurispay.domain.receivable.model.Receivable;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ReceivableApplicationMapper {

    ReceivableResponse toResponse(Receivable receivable);
}
