package com.example.mercadoxcontext.utils;

import java.util.Optional;
import java.util.UUID;

public final class OrgIdContextHolder {

    private static final ThreadLocal<UUID> context = new ThreadLocal<>();

    private OrgIdContextHolder() {
    }

    public static void setTenantId(UUID tenantId) {
        context.set(tenantId);
    }

    public static UUID getTenantId() {
        return context.get();
    }

    public static boolean hasTenantId() {
        return Optional.ofNullable(context.get()).isPresent();
    }

    public static void clear() {
        context.remove();
    }
}
