package com.example.mercadoxcontext.utils;

public final class OrgIdContextHolder {

    private static final ThreadLocal<String> context = new ThreadLocal<>();

    private OrgIdContextHolder() {
        // Prevent instantiation
    }

    public static void setTenantId(String tenantId) {
        context.set(tenantId);
    }

    public static String getTenantId() {
        return context.get();
    }

    public static void clear() {
        context.remove();
    }
}
