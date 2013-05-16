package com.palominolabs.metrics.guice.servlet;

import com.google.inject.Module;

public class AdminServletModuleCustomPathsTest extends AdminServletModuleTestCase {
    @Override
    protected Module getAdminServletModule() {
        return new AdminServletModule(getHealthCheckPath(), getMetricsPath(), getPingPath(), getThreadsPath());
    }

    @Override
    protected String getMetricsPath() {
        return "/asdf/metrics";
    }

    @Override
    protected String getPingPath() {
        return "/foo/ping";
    }

    @Override
    protected String getThreadsPath() {
        return "/baz/threads";
    }

    @Override
    protected String getHealthCheckPath() {
        return "/qwerty/health";
    }
}
