package hn.shadowcore.mercadox.context.utils;

import org.apache.kafka.clients.producer.ProducerRecord;

import java.nio.charset.StandardCharsets;

public class KafkaProducerRecordFactory {

    public static <T> ProducerRecord<String, T> buildWithOrgIdHeader(String topic, String key, T eventPayload) {
        ProducerRecord<String, T> producerRecord;
        if(key != null) {
            producerRecord = new ProducerRecord<>(topic, key, eventPayload);
        }
        else {
            producerRecord = new ProducerRecord<>(topic, eventPayload);
        }
        String tenantId = OrgIdContextHolder.getTenantId();
        if(tenantId != null) {
            producerRecord.headers().add("x-org-id", OrgIdContextHolder.getTenantId()
                    .getBytes(StandardCharsets.UTF_8));
        }
        addCorrelationIdHeader(producerRecord);
        return producerRecord;
    }

    public static <T> ProducerRecord<String, T> buildWithoutOrgIdHeader(String topic, String key, T eventPayload) {
        ProducerRecord<String, T> producerRecord;
        if(key != null) {
            producerRecord = new ProducerRecord<>(topic, key, eventPayload);
        }
        else{
            producerRecord = new ProducerRecord<>(topic, eventPayload);
        }
        addCorrelationIdHeader(producerRecord);
        return producerRecord;
    }

    // Independent of org-id — attached on every record (both variants above) so tracing
    // isn't limited to tenant-scoped topics.
    private static <T> void addCorrelationIdHeader(ProducerRecord<String, T> producerRecord) {
        String correlationId = CorrelationIdContext.get();
        if (correlationId != null) {
            producerRecord.headers().add("x-correlation-id", correlationId.getBytes(StandardCharsets.UTF_8));
        }
    }
}
