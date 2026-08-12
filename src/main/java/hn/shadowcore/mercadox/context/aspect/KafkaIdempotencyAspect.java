package hn.shadowcore.mercadox.context.aspect;

import hn.shadowcore.mercadox.library.entity.model.enums.kafka.event.DomainEvent;
import hn.shadowcore.mercadox.library.redis.util.RedisIdempotencyChecker;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class KafkaIdempotencyAspect {

    private final RedisIdempotencyChecker idempotencyChecker;

    @Around("@annotation(hn.shadowcore.mercadox.context.utils.annotations.KafkaIdempotent)")
    public Object checkIdempotency(ProceedingJoinPoint pjp) throws Throwable {
        DomainEvent dto = null;

        Object[] args = pjp.getArgs();

        for (Object arg : args) {
            if (arg instanceof ConsumerRecord<?, ?> consumerRecord) {
                Object value = consumerRecord.value();
                if (value instanceof DomainEvent typedDto) {
                    dto = typedDto;
                    break;
                }

            } else if (arg instanceof DomainEvent directDto) {
                dto = directDto;
                break;
            }
        }
        if (!Objects.nonNull(dto) || !Objects.nonNull(dto.getEventId())) {
            log.info("Unable to check duplicates on redis idempotency checker due to null values being passed.");
            return null;
        }
        if(idempotencyChecker.isDuplicate(dto.getEventId())) {
            log.info("Tried to run duplicate event with ID: {}", dto.getEventId());
            return null;
        }

        Object result = pjp.proceed();
        idempotencyChecker.markProcessed(dto.getEventId());
        return result;
    }

}
