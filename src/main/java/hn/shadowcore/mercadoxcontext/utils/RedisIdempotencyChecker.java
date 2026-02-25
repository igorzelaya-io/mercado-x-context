package hn.shadowcore.mercadoxcontext.utils;


import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.time.Duration;

@Component
@RequiredArgsConstructor
public class RedisIdempotencyChecker {

    private final StringRedisTemplate redisTemplate;
    private static final String PREFIX = "eventId:";

    public boolean isDuplicate(String eventId) {
        Boolean alreadyExists = redisTemplate.hasKey(PREFIX + eventId);
        if(Boolean.TRUE.equals(alreadyExists)) {
            return true;
        }
        redisTemplate.opsForValue().set(getKey(eventId), eventId, Duration.ofHours(24));
        return false;
    }

    private String getKey(String uuid) {
        return PREFIX + uuid;
    }

}
