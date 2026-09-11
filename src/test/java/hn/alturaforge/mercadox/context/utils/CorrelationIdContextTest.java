package hn.alturaforge.mercadox.context.utils;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.slf4j.MDC;

import static org.assertj.core.api.Assertions.assertThat;

class CorrelationIdContextTest {

    @AfterEach
    void tearDown() {
        CorrelationIdContext.clear();
    }

    @Test
    void set_putsTheValueInMdcUnderTheExpectedKey() {
        CorrelationIdContext.set("corr-123");

        assertThat(MDC.get("correlationId")).isEqualTo("corr-123");
    }

    @Test
    void get_readsBackWhatWasSet() {
        CorrelationIdContext.set("corr-123");

        assertThat(CorrelationIdContext.get()).isEqualTo("corr-123");
    }

    @Test
    void get_returnsNullWhenNothingSet() {
        assertThat(CorrelationIdContext.get()).isNull();
    }

    @Test
    void clear_removesTheValue() {
        CorrelationIdContext.set("corr-123");

        CorrelationIdContext.clear();

        assertThat(CorrelationIdContext.get()).isNull();
    }
}
