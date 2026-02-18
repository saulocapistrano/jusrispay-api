package br.com.jurispay.application.loantype.validator;

import br.com.jurispay.api.dto.loantype.LoanTypeRequest;
import br.com.jurispay.domain.exception.common.ErrorCode;
import br.com.jurispay.domain.exception.common.ValidationException;
import br.com.jurispay.domain.loantype.model.LoanTypeScheduleType;
import org.springframework.stereotype.Component;

import java.time.DayOfWeek;

@Component
public class LoanTypeRequestValidator {

    public void validate(LoanTypeRequest request) {
        if (request == null) {
            throw new ValidationException(ErrorCode.REQUIRED_FIELD, "Request é obrigatório.");
        }
        if (request.getNome() == null || request.getNome().isBlank()) {
            throw new ValidationException(ErrorCode.REQUIRED_FIELD, "Nome é obrigatório.");
        }
        if (request.getIntervaloPagamentoDias() == null || request.getIntervaloPagamentoDias() < 1) {
            throw new ValidationException(ErrorCode.INVALID_VALUE, "intervaloPagamentoDias deve ser >= 1.");
        }
        if (request.getScheduleType() == null || request.getScheduleType().isBlank()) {
            throw new ValidationException(ErrorCode.REQUIRED_FIELD, "scheduleType é obrigatório.");
        }

        LoanTypeScheduleType scheduleType;
        try {
            scheduleType = LoanTypeScheduleType.valueOf(request.getScheduleType().trim().toUpperCase());
        } catch (IllegalArgumentException ex) {
            throw new ValidationException(ErrorCode.INVALID_VALUE, "scheduleType inválido.");
        }

        if (scheduleType == LoanTypeScheduleType.WEEKLY_FIXED_DAY) {
            if (request.getWeeklyDayOfWeek() == null || request.getWeeklyDayOfWeek().isBlank()) {
                throw new ValidationException(ErrorCode.REQUIRED_FIELD, "weeklyDayOfWeek é obrigatório para semanal.");
            }
            DayOfWeek parsed;
            try {
                parsed = DayOfWeek.valueOf(request.getWeeklyDayOfWeek().trim().toUpperCase());
            } catch (IllegalArgumentException ex) {
                throw new ValidationException(ErrorCode.INVALID_VALUE, "weeklyDayOfWeek inválido.");
            }
            if (parsed != DayOfWeek.SATURDAY && parsed != DayOfWeek.SUNDAY) {
                throw new ValidationException(ErrorCode.INVALID_VALUE, "Semanal permite apenas SATURDAY ou SUNDAY.");
            }

            if (request.getIntervaloPagamentoDias() != 7) {
                throw new ValidationException(ErrorCode.INVALID_VALUE, "Semanal deve ter intervaloPagamentoDias = 7.");
            }
        }
    }
}
