package br.com.jurispay.domain.risk.scoring;

public record PillarScore(
        Object raw,
        Integer normalized,
        double weight,
        int contribution
) {
}
