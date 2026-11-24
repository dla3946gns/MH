package com.sps.config;

public final class RuntimeConfig {
    private RuntimeConfig() {}

    public static String require(String name) {
        String value = System.getenv(name);
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalStateException("Missing required environment variable: " + name);
        }
        return value;
    }
}
