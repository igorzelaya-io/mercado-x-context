package hn.shadowcore.mercadoxcontext.aspect;


import hn.shadowcore.mercadoxcontext.utils.OrgIdContextHolder;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.header.Header;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;

@Aspect
@Component
public class KafkaOrgIdPropagationAspect {

    @Around("@annotation(hn.shadowcore.mercadoxemail.util.KafkaOrgIdPropagated)")
    public Object injectOrgIdFromKafkaHeader(ProceedingJoinPoint joinPoint) throws Throwable {
        try {
            Object[] args = joinPoint.getArgs();
            for(Object arg : args) {
                if(arg instanceof ConsumerRecord<?,?> record) {
                    Header header = record.headers().lastHeader("x-org-id");
                    if(header != null) {
                        String tenantId = new String(header.value(), StandardCharsets.UTF_8);
                        OrgIdContextHolder.setTenantId(tenantId);
                        break;
                    }
                }
            }
            return joinPoint.proceed();
        }
        finally {
            OrgIdContextHolder.clear();
        }

    }


}
