package hn.shadowcore.mercadox.context.listener;

import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class DltEventListener {

    @KafkaListener(
            topicPattern = ".*\\.DLT",
            groupId = "${spring.application.name:mercadox}-dlt-group",
            containerFactory = "deadLetterListenerContainerFactory"
    )
    public void handle(
            @Payload(required = false) byte[] payload,
            @Header(KafkaHeaders.RECEIVED_TOPIC) String topic,
            @Header(value = KafkaHeaders.RECEIVED_KEY, required = false) String key,
            @Header(value = KafkaHeaders.DLT_ORIGINAL_TOPIC, required = false) String originalTopic,
            @Header(value = KafkaHeaders.DLT_ORIGINAL_OFFSET, required = false) Long originalOffset,
            @Header(value = KafkaHeaders.DLT_EXCEPTION_FQCN, required = false) String exceptionClass,
            @Header(value = KafkaHeaders.DLT_EXCEPTION_MESSAGE, required = false) String exceptionMessage,
            @Header(value = KafkaHeaders.DLT_EXCEPTION_CAUSE_FQCN, required = false) String causeMessage) {

        log.error("""
                Dead-letter event received.
                  DLT topic        : {}
                  Original topic   : {}
                  Original offset  : {}
                  Message key      : {}
                  Exception        : {}
                  Message          : {}
                  Cause            : {}
                  Payload size     : {} bytes
                """,
                topic,
                originalTopic,
                originalOffset,
                key,
                exceptionClass,
                exceptionMessage,
                causeMessage,
                payload != null ? payload.length : 0);
    }
}
