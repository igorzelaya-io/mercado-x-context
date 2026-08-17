package hn.shadowcore.mercadox.context.aspect;

import hn.shadowcore.mercadox.library.entity.model.enums.kafka.event.DomainEvent;
import hn.shadowcore.mercadox.library.redis.util.RedisIdempotencyChecker;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.avro.Schema;
import org.apache.avro.specific.SpecificRecord;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class KafkaIdempotencyAspect {

    private final RedisIdempotencyChecker idempotencyChecker;

    @Around("@annotation(hn.shadowcore.mercadox.context.utils.annotations.KafkaIdempotent)")
    public Object checkIdempotency(ProceedingJoinPoint pjp) throws Throwable {
        String eventId = null;

        for (Object arg : pjp.getArgs()) {
            eventId = extractEventId(arg instanceof ConsumerRecord<?, ?> r ? r.value() : arg);
            if (eventId != null) break;
        }

        if (eventId == null) {
            log.info("Unable to check duplicates: no eventId found on incoming record.");
            return null;
        }
        if (idempotencyChecker.isDuplicate(eventId)) {
            log.info("Dropped duplicate event with ID: {}", eventId);
            return null;
        }

        Object result = pjp.proceed();
        idempotencyChecker.markProcessed(eventId);
        return result;
    }

    private String extractEventId(Object value) {
        if (value instanceof DomainEvent domainEvent) {
            return domainEvent.getEventId();
        }
        if (value instanceof SpecificRecord record) {
            Schema.Field field = record.getSchema().getField("eventId");
            if (field != null) {
                Object result = record.get(field.pos());
                return result instanceof String s ? s : null;
            }
        }
        return null;
    }
}
