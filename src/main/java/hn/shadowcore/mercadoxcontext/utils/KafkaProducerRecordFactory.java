package hn.shadowcore.mercadoxcontext.utils;

import hn.shadowcore.mercadoxlibrary.entity.response.dto.EmailEventDto;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.nio.charset.StandardCharsets;

public class KafkaProducerRecordFactory<T> {

    public ProducerRecord<String, EmailEventDto<T>> generateOrgIdHeaders(String topic, EmailEventDto<T> eventDto) {
        ProducerRecord<String, EmailEventDto<T>> producerRecord = new ProducerRecord<>(topic, eventDto);
        producerRecord.headers().add("x-org-id", OrgIdContextHolder.getTenantId().getBytes(StandardCharsets.UTF_8));
        return producerRecord;
    }
}
