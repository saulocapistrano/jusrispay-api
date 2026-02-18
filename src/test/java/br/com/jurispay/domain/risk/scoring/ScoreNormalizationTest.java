package br.com.jurispay.domain.risk.scoring;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ScoreNormalizationTest {

    @Test
    void shouldNormalizeBureauScoreTo0To1000() {
        assertEquals(0, ScoreNormalization.normalizeBureauScore(300));
        assertEquals(500, ScoreNormalization.normalizeBureauScore(600));
        assertEquals(1000, ScoreNormalization.normalizeBureauScore(900));
    }

    @Test
    void shouldClampBureauScoreBeforeNormalization() {
        assertEquals(0, ScoreNormalization.normalizeBureauScore(0));
        assertEquals(1000, ScoreNormalization.normalizeBureauScore(2000));
    }

    @Test
    void shouldClamp0To1000() {
        assertEquals(0, ScoreNormalization.clamp0To1000(-10));
        assertEquals(0, ScoreNormalization.clamp0To1000(0));
        assertEquals(10, ScoreNormalization.clamp0To1000(10));
        assertEquals(1000, ScoreNormalization.clamp0To1000(1000));
        assertEquals(1000, ScoreNormalization.clamp0To1000(1001));
    }

    @Test
    void shouldThrowWhenBureauScoreIsNull() {
        assertThrows(IllegalArgumentException.class, () -> ScoreNormalization.normalizeBureauScore(null));
    }
}
