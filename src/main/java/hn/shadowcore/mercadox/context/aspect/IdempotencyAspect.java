package hn.shadowcore.mercadox.context.aspect;

import hn.shadowcore.mercadox.context.utils.annotations.IdempotentOperation;
import hn.shadowcore.mercadox.library.entity.request.IdempotentRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.time.Duration;

@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class IdempotencyAspect {

    private final StringRedisTemplate redisTemplate;

    @Around("@annotation(idempotentOperation)")
    public Object checkIdempotency(ProceedingJoinPoint pjp, IdempotentOperation idempotentOperation) throws Throwable {
        String key = resolveKey(pjp.getArgs());

        if (key == null) {
            log.warn("@IdempotentOperation on '{}' has no IdempotentRequest argument — skipping check.",
                    pjp.getSignature().getName());
            return pjp.proceed();
        }

        String redisKey = idempotentOperation.keyPrefix() + key;
        Duration ttl = Duration.ofMinutes(idempotentOperation.ttlMinutes());

        // Atomic SET NX EX — avoids the TOCTOU gap of separate isDuplicate + markProcessed calls
        Boolean accepted = redisTemplate.opsForValue().setIfAbsent(redisKey, "1", ttl);

        if (!Boolean.TRUE.equals(accepted)) {
            log.info("Duplicate request blocked for key: {}", redisKey);
            throw new IllegalStateException(
                    String.format("Duplicate request: this operation was already submitted (key='%s'). " +
                            "Please wait before retrying.", key));
        }

        try {
            return pjp.proceed();
        } catch (Throwable t) {
            // Roll back so the client can retry after a genuine server error
            redisTemplate.delete(redisKey);
            throw t;
        }
    }

    private String resolveKey(Object[] args) {
        for (Object arg : args) {
            if (arg instanceof IdempotentRequest req) {
                return req.getIdempotencyKey();
            }
        }
        return null;
    }

}
