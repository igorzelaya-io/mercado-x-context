package hn.shadowcore.mercadox.context.aspect;

import hn.shadowcore.mercadox.context.exception.InvalidEventIdException;
import hn.shadowcore.mercadox.library.entity.model.enums.kafka.event.DomainEvent;
import hn.shadowcore.mercadox.library.redis.util.RedisIdempotencyChecker;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
            throw new InvalidEventIdException(
                    "Rejecting event with missing eventId on topic — routed to DLT for inspection.");
        }
        if (!idempotencyChecker.claimProcessing(eventId)) {
            log.info("Dropped duplicate event with ID: {}", eventId);
            return null;
        }

        return pjp.proceed();
    }

    private String extractEventId(Object value) {
        if (value instanceof DomainEvent domainEvent) {
            return domainEvent.getEventId();
        }
        if (value != null) {
            // Use reflection to call getEventId() directly on the generated Avro class.
            // The schema-field position lookup (record.get(field.pos())) is fragile when
            // the runtime schema from the Schema Registry has different field ordering
            // than the compile-time schema — direct method invocation has no such ambiguity.
            try {
                Object result = value.getClass().getMethod("getEventId").invoke(value);
                return result instanceof String s ? s : null;
            } catch (NoSuchMethodException ignored) {
                // Not an event type that carries eventId
            } catch (Exception e) {
                log.debug("Could not extract eventId from {}", value.getClass().getSimpleName());
            }
        }
        return null;
    }
}
