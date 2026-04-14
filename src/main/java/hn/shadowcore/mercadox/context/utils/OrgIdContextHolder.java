package hn.shadowcore.mercadox.context.utils;

import java.util.Optional;

public final class OrgIdContextHolder {

    private static final ThreadLocal<String> context = new ThreadLocal<>();

    private OrgIdContextHolder() {
    }

    public static void setTenantId(String tenantId) {
        context.set(tenantId);
    }

    public static String getTenantId() {
        return context.get();
    }

    public static boolean hasTenantId() {
        return Optional.ofNullable(context.get()).isPresent();
    }

    public static void clear() {
        context.remove();
    }
}
