package com.palominolabs.metrics.guice.servlet;

import com.codahale.metrics.servlets.AdminServlet;

public class AdminServletModuleDefaultPathsTest extends AdminServletModuleTestCase {
    protected AdminServletModule getAdminServletModule() {
        return new AdminServletModule();
    }

    protected String getMetricsPath() {
        return AdminServlet.DEFAULT_METRICS_URI;
    }

    protected String getPingPath() {
        return AdminServlet.DEFAULT_PING_URI;
    }

    protected String getThreadsPath() {
        return AdminServlet.DEFAULT_THREADS_URI;
    }

    protected String getHealthCheckPath() {
        return AdminServlet.DEFAULT_HEALTHCHECK_URI;
    }
}
