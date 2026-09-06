package hn.shadowcore.mercadox.context.utils;

import org.apache.kafka.clients.producer.ProducerRecord;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;

import static org.assertj.core.api.Assertions.assertThat;

class KafkaProducerRecordFactoryTest {

    @AfterEach
    void tearDown() {
        OrgIdContextHolder.clear();
        CorrelationIdContext.clear();
    }

    @Test
    void buildWithOrgIdHeader_attachesBothHeadersWhenBothContextsSet() {
        OrgIdContextHolder.setTenantId("org-123");
        CorrelationIdContext.set("corr-456");

        ProducerRecord<String, String> record =
                KafkaProducerRecordFactory.buildWithOrgIdHeader("topic", "key", "payload");

        assertThat(headerValue(record, "x-org-id")).isEqualTo("org-123");
        assertThat(headerValue(record, "x-correlation-id")).isEqualTo("corr-456");
    }

    @Test
    void buildWithOrgIdHeader_omitsCorrelationIdHeaderWhenContextEmpty() {
        OrgIdContextHolder.setTenantId("org-123");

        ProducerRecord<String, String> record =
                KafkaProducerRecordFactory.buildWithOrgIdHeader("topic", "key", "payload");

        assertThat(record.headers().lastHeader("x-correlation-id")).isNull();
    }

    @Test
    void buildWithoutOrgIdHeader_stillAttachesCorrelationIdHeaderWhenContextSet() {
        CorrelationIdContext.set("corr-456");

        ProducerRecord<String, String> record =
                KafkaProducerRecordFactory.buildWithoutOrgIdHeader("topic", "key", "payload");

        assertThat(record.headers().lastHeader("x-org-id")).isNull();
        assertThat(headerValue(record, "x-correlation-id")).isEqualTo("corr-456");
    }

    private String headerValue(ProducerRecord<?, ?> record, String key) {
        return new String(record.headers().lastHeader(key).value(), StandardCharsets.UTF_8);
    }
}
