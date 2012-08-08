package com.palominolabs.metrics.guice;

import com.yammer.metrics.core.MetricName;

import java.lang.reflect.Method;

/**
 * Re-implements some now-private methods in {@link MetricName}.
 */
final class MetricNames {

    static String chooseDomain(String domain, Class<?> klass) {
        if (domain == null || domain.isEmpty()) {
            domain = getPackageName(klass);
        }
        return domain;
    }

    static String chooseType(String type, Class<?> klass) {
        if (type == null || type.isEmpty()) {
            type = getClassName(klass);
        }
        return type;
    }

    static String chooseName(String name, Method method) {
        if (name == null || name.isEmpty()) {
            name = method.getName();
        }
        return name;
    }

    private static String getPackageName(Class<?> klass) {
        return klass.getPackage() == null ? "" : klass.getPackage().getName();
    }

    private static String getClassName(Class<?> klass) {
        return klass.getSimpleName().replaceAll("\\$$", "");
    }
}
