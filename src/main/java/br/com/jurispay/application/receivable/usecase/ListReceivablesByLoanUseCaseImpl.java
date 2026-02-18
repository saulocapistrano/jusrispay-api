package br.com.jurispay.application.receivable.usecase;

import br.com.jurispay.application.receivable.dto.ReceivableResponse;
import br.com.jurispay.application.receivable.mapper.ReceivableApplicationMapper;
import br.com.jurispay.domain.exception.common.ErrorCode;
import br.com.jurispay.domain.exception.common.ValidationException;
import br.com.jurispay.domain.receivable.repository.ReceivableRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ListReceivablesByLoanUseCaseImpl implements ListReceivablesByLoanUseCase {

    private final ReceivableRepository receivableRepository;
    private final ReceivableApplicationMapper mapper;

    public ListReceivablesByLoanUseCaseImpl(
            ReceivableRepository receivableRepository,
            ReceivableApplicationMapper mapper) {
        this.receivableRepository = receivableRepository;
        this.mapper = mapper;
    }

    @Override
    public List<ReceivableResponse> listByLoanId(Long loanId) {
        if (loanId == null) {
            throw new ValidationException(ErrorCode.REQUIRED_FIELD, "loanId é obrigatório.");
        }

        return receivableRepository.findByLoanId(loanId).stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());
    }
}
