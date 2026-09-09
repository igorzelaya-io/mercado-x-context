package hn.shadowcore.mercadox.context.utils;

import org.slf4j.MDC;

/**
 * Thin wrapper over SLF4J's MDC so the correlation ID is both programmatically
 * readable (to persist on a Conversation, or attach to an outbound Kafka header)
 * and automatically present in every log line for the current thread, without a
 * second thread-local copy to keep in sync with MDC.
 */
public final class CorrelationIdContext {

    public static final String MDC_KEY = "correlationId";

    private CorrelationIdContext() {
    }

    public static void set(String correlationId) {
        MDC.put(MDC_KEY, correlationId);
    }

    public static String get() {
        return MDC.get(MDC_KEY);
    }

    public static void clear() {
        MDC.remove(MDC_KEY);
    }
}
