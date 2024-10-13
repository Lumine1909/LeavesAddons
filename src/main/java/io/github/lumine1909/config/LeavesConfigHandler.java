package io.github.lumine1909.config;

import java.lang.reflect.Field;

public class LeavesConfigHandler {

    private final Class<?> c_LeavesConfig;

    public LeavesConfigHandler() {
        try {
            c_LeavesConfig = Class.forName("org.leavesmc.leaves.LeavesConfig");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public int getInt(String key) {
        try {
            Field f_value = c_LeavesConfig.getDeclaredField(key);
            f_value.setAccessible(true);
            return f_value.getInt(null);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public boolean getBoolean(String key) {
        try {
            Field f_value = c_LeavesConfig.getDeclaredField(key);
            f_value.setAccessible(true);
            return f_value.getBoolean(null);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public String getString(String key) {
        try {
            Field f_value = c_LeavesConfig.getDeclaredField(key);
            f_value.setAccessible(true);
            return (String) f_value.get(null);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
