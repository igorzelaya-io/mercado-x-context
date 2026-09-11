package hn.alturaforge.mercadox.context.aspect;

import hn.alturaforge.mercadox.context.utils.CorrelationIdContext;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.header.internals.RecordHeader;
import org.apache.kafka.common.header.internals.RecordHeaders;
import org.aspectj.lang.ProceedingJoinPoint;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.nio.charset.StandardCharsets;
import java.util.concurrent.atomic.AtomicReference;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class KafkaCorrelationIdPropagationAspectTest {

    @Mock
    private ProceedingJoinPoint joinPoint;

    private final KafkaCorrelationIdPropagationAspect aspect = new KafkaCorrelationIdPropagationAspect();

    @AfterEach
    void tearDown() {
        CorrelationIdContext.clear();
    }

    @Test
    void injectCorrelationIdFromKafkaHeader_setsContextFromHeaderDuringProceedThenClears() throws Throwable {
        RecordHeaders headers = new RecordHeaders();
        headers.add(new RecordHeader("x-correlation-id", "corr-abc".getBytes(StandardCharsets.UTF_8)));
        ConsumerRecord<String, Object> consumerRecord =
                new ConsumerRecord<>("topic", 0, 0L, 0L, null, null, 0, 0, "key", "value", headers, null);

        when(joinPoint.getArgs()).thenReturn(new Object[]{consumerRecord});
        AtomicReference<String> seenDuringProceed = new AtomicReference<>();
        when(joinPoint.proceed()).thenAnswer(invocation -> {
            seenDuringProceed.set(CorrelationIdContext.get());
            return null;
        });

        aspect.injectCorrelationIdFromKafkaHeader(joinPoint);

        assertThat(seenDuringProceed.get()).isEqualTo("corr-abc");
        assertThat(CorrelationIdContext.get()).isNull();
    }

    @Test
    void injectCorrelationIdFromKafkaHeader_whenHeaderAbsent_proceedsWithoutSettingContext() throws Throwable {
        ConsumerRecord<String, Object> consumerRecord =
                new ConsumerRecord<>("topic", 0, 0L, "key", "value");

        when(joinPoint.getArgs()).thenReturn(new Object[]{consumerRecord});
        AtomicReference<String> seenDuringProceed = new AtomicReference<>();
        when(joinPoint.proceed()).thenAnswer(invocation -> {
            seenDuringProceed.set(CorrelationIdContext.get());
            return null;
        });

        aspect.injectCorrelationIdFromKafkaHeader(joinPoint);

        assertThat(seenDuringProceed.get()).isNull();
    }

    @Test
    void injectCorrelationIdFromKafkaHeader_clearsContextEvenWhenProceedThrows() throws Throwable {
        RecordHeaders headers = new RecordHeaders();
        headers.add(new RecordHeader("x-correlation-id", "corr-abc".getBytes(StandardCharsets.UTF_8)));
        ConsumerRecord<String, Object> consumerRecord =
                new ConsumerRecord<>("topic", 0, 0L, 0L, null, null, 0, 0, "key", "value", headers, null);

        when(joinPoint.getArgs()).thenReturn(new Object[]{consumerRecord});
        when(joinPoint.proceed()).thenThrow(new RuntimeException("boom"));

        try {
            aspect.injectCorrelationIdFromKafkaHeader(joinPoint);
        } catch (RuntimeException expected) {
            // expected — just verifying cleanup still ran
        }

        assertThat(CorrelationIdContext.get()).isNull();
    }
}
