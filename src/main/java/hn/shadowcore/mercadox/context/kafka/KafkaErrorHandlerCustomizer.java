package hn.shadowcore.mercadox.context.kafka;

import org.springframework.kafka.listener.DefaultErrorHandler;

/**
 * Implement this interface in any service to register service-specific
 * non-retryable (or retryable) exception classifications on the shared
 * Kafka error handler — without coupling mercado-x-context to individual
 * service exception types.
 *
 * All beans of this type are collected by KafkaPubSubConfig and applied
 * to the DefaultErrorHandler before it is used by any listener container.
 */
public interface KafkaErrorHandlerCustomizer {

    void customize(DefaultErrorHandler errorHandler);

}
