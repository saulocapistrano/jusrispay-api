package br.com.jurispay.domain.risk.scoring;

public final class ScoreNormalization {

    private static final int BUREAU_MIN = 300;
    private static final int BUREAU_MAX = 900;

    private ScoreNormalization() {
    }

    public static int normalizeBureauScore(Integer bureauScore) {
        if (bureauScore == null) {
            throw new IllegalArgumentException("bureauScore is required");
        }
        double clamped = Math.max(BUREAU_MIN, Math.min(BUREAU_MAX, bureauScore));
        double normalized = ((clamped - BUREAU_MIN) / (double) (BUREAU_MAX - BUREAU_MIN)) * 1000.0;
        return (int) Math.round(normalized);
    }

    public static int clamp0To1000(int value) {
        return Math.max(0, Math.min(1000, value));
    }
}
