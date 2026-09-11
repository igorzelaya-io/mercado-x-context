package hn.alturaforge.mercadox.context.aspect;

import hn.alturaforge.mercadox.context.utils.CorrelationIdContext;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.header.Header;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;

@Aspect
@Component
public class KafkaCorrelationIdPropagationAspect {

    @Around("@annotation(hn.alturaforge.mercadox.context.utils.annotations.KafkaCorrelationIdPropagated)")
    public Object injectCorrelationIdFromKafkaHeader(ProceedingJoinPoint joinPoint) throws Throwable {
        try {
            Object[] args = joinPoint.getArgs();
            for (Object arg : args) {
                if (arg instanceof ConsumerRecord<?, ?> consumerRecord) {
                    Header header = consumerRecord.headers().lastHeader("x-correlation-id");
                    if (header != null) {
                        String correlationId = new String(header.value(), StandardCharsets.UTF_8);
                        CorrelationIdContext.set(correlationId);
                        break;
                    }
                }
            }
            return joinPoint.proceed();
        } finally {
            CorrelationIdContext.clear();
        }
    }
}
