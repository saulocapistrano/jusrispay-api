package br.com.jurispay.domain.loan.service;

import br.com.jurispay.domain.exception.common.ErrorCode;
import br.com.jurispay.domain.exception.common.ValidationException;
import br.com.jurispay.domain.loan.model.LoanPaymentPeriod;
import br.com.jurispay.domain.loantype.model.LoanType;
import br.com.jurispay.domain.loantype.model.LoanTypeScheduleType;
import org.springframework.stereotype.Component;

import java.time.DayOfWeek;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.time.temporal.TemporalAdjusters;

@Component
public class InstallmentScheduleService {

    public LoanPaymentPeriod resolvePaymentPeriod(LoanType loanType) {
        if (loanType == null) {
            throw new ValidationException(ErrorCode.BUSINESS_RULE_VIOLATION, "Tipo de empréstimo é obrigatório.");
        }
        LoanTypeScheduleType scheduleType = loanType.getScheduleType();
        if (scheduleType == LoanTypeScheduleType.WEEKLY_FIXED_DAY) {
            return LoanPaymentPeriod.WEEKLY;
        }

        Integer intervalDays = loanType.getIntervaloPagamentoDias();
        if (intervalDays == null) {
            throw new ValidationException(ErrorCode.BUSINESS_RULE_VIOLATION, "intervaloPagamentoDias é obrigatório.");
        }

        return switch (intervalDays) {
            case 1 -> LoanPaymentPeriod.DAILY;
            case 7 -> LoanPaymentPeriod.WEEKLY;
            case 10 -> LoanPaymentPeriod.TEN_DAYS;
            case 15 -> LoanPaymentPeriod.FIFTEEN_DAYS;
            case 20 -> LoanPaymentPeriod.TWENTY_DAYS;
            case 30 -> LoanPaymentPeriod.MONTHLY;
            default -> throw new ValidationException(ErrorCode.BUSINESS_RULE_VIOLATION, "Intervalo em dias não suportado: " + intervalDays);
        };
    }

    public LocalDate resolveStartDateFromCreditDate(Instant dataLiberacao) {
        if (dataLiberacao == null) {
            throw new ValidationException(ErrorCode.BUSINESS_RULE_VIOLATION, "dataLiberacao é obrigatória para cálculo de parcelas.");
        }
        return LocalDate.ofInstant(dataLiberacao, ZoneOffset.UTC).plusDays(1);
    }

    public LocalDate firstDueDate(LoanType loanType, LocalDate startDate) {
        if (loanType == null) {
            throw new ValidationException(ErrorCode.BUSINESS_RULE_VIOLATION, "Tipo de empréstimo é obrigatório.");
        }
        if (startDate == null) {
            throw new ValidationException(ErrorCode.BUSINESS_RULE_VIOLATION, "startDate é obrigatório.");
        }

        LoanTypeScheduleType scheduleType = loanType.getScheduleType();
        if (scheduleType == null) {
            throw new ValidationException(ErrorCode.BUSINESS_RULE_VIOLATION, "scheduleType do tipo de empréstimo é obrigatório.");
        }

        if (scheduleType == LoanTypeScheduleType.INTERVAL_DAYS) {
            return startDate;
        }

        if (scheduleType == LoanTypeScheduleType.WEEKLY_FIXED_DAY) {
            DayOfWeek dayOfWeek = parseWeeklyDayOfWeek(loanType.getWeeklyDayOfWeek());
            return startDate.with(TemporalAdjusters.nextOrSame(dayOfWeek));
        }

        throw new ValidationException(ErrorCode.BUSINESS_RULE_VIOLATION, "scheduleType não suportado: " + scheduleType);
    }

    public int calculateInstallments(LoanType loanType, LocalDate startDate, LocalDate dataPrevistaDevolucao) {
        if (dataPrevistaDevolucao == null) {
            throw new ValidationException(ErrorCode.BUSINESS_RULE_VIOLATION, "dataPrevistaDevolucao é obrigatória.");
        }

        LocalDate first = firstDueDate(loanType, startDate);
        if (dataPrevistaDevolucao.isBefore(first)) {
            throw new ValidationException(ErrorCode.BUSINESS_RULE_VIOLATION, "Data prevista de devolução deve ser posterior ao primeiro vencimento.");
        }

        LoanTypeScheduleType scheduleType = loanType.getScheduleType();
        if (scheduleType == LoanTypeScheduleType.INTERVAL_DAYS) {
            Integer intervalDays = loanType.getIntervaloPagamentoDias();
            if (intervalDays == null || intervalDays < 1) {
                throw new ValidationException(ErrorCode.BUSINESS_RULE_VIOLATION, "intervaloPagamentoDias inválido.");
            }
            long diffDays = java.time.temporal.ChronoUnit.DAYS.between(first, dataPrevistaDevolucao);
            return Math.toIntExact(diffDays / intervalDays) + 1;
        }

        if (scheduleType == LoanTypeScheduleType.WEEKLY_FIXED_DAY) {
            long diffDays = java.time.temporal.ChronoUnit.DAYS.between(first, dataPrevistaDevolucao);
            return Math.toIntExact(diffDays / 7) + 1;
        }

        throw new ValidationException(ErrorCode.BUSINESS_RULE_VIOLATION, "scheduleType não suportado: " + scheduleType);
    }

    public LocalDate dueDateForInstallment(LoanType loanType, LocalDate startDate, int installmentNumber) {
        if (installmentNumber < 1) {
            throw new ValidationException(ErrorCode.BUSINESS_RULE_VIOLATION, "installmentNumber inválido.");
        }
        LocalDate first = firstDueDate(loanType, startDate);

        LoanTypeScheduleType scheduleType = loanType.getScheduleType();
        if (scheduleType == LoanTypeScheduleType.INTERVAL_DAYS) {
            int intervalDays = loanType.getIntervaloPagamentoDias();
            return first.plusDays((long) (installmentNumber - 1) * intervalDays);
        }

        if (scheduleType == LoanTypeScheduleType.WEEKLY_FIXED_DAY) {
            return first.plusWeeks(installmentNumber - 1L);
        }

        throw new ValidationException(ErrorCode.BUSINESS_RULE_VIOLATION, "scheduleType não suportado: " + scheduleType);
    }

    private DayOfWeek parseWeeklyDayOfWeek(String weeklyDayOfWeek) {
        if (weeklyDayOfWeek == null || weeklyDayOfWeek.isBlank()) {
            throw new ValidationException(ErrorCode.BUSINESS_RULE_VIOLATION, "weeklyDayOfWeek é obrigatório para tipo semanal.");
        }
        try {
            DayOfWeek parsed = DayOfWeek.valueOf(weeklyDayOfWeek.trim().toUpperCase());
            if (parsed != DayOfWeek.SATURDAY && parsed != DayOfWeek.SUNDAY) {
                throw new ValidationException(ErrorCode.BUSINESS_RULE_VIOLATION, "Semanal permite apenas SATURDAY ou SUNDAY.");
            }
            return parsed;
        } catch (IllegalArgumentException ex) {
            throw new ValidationException(ErrorCode.BUSINESS_RULE_VIOLATION, "weeklyDayOfWeek inválido: " + weeklyDayOfWeek);
        }
    }
}
