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
        return producerRecord;
    }
}
