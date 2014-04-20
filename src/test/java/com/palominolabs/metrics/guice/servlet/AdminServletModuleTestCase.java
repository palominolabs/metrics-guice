package com.palominolabs.metrics.guice.servlet;

import com.codahale.metrics.Gauge;
import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.health.HealthCheck;
import com.codahale.metrics.health.HealthCheckRegistry;
import com.codahale.metrics.servlets.HealthCheckServlet;
import com.codahale.metrics.servlets.MetricsServlet;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Module;
import com.google.inject.Stage;
import com.google.inject.servlet.GuiceFilter;
import com.ning.http.client.AsyncCompletionHandler;
import com.ning.http.client.AsyncHttpClient;
import com.ning.http.client.Response;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.server.handler.HandlerCollection;
import org.eclipse.jetty.servlet.FilterHolder;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.bridge.SLF4JBridgeHandler;

import javax.servlet.DispatcherType;
import java.io.IOException;
import java.util.EnumSet;
import java.util.concurrent.ExecutionException;
import java.util.logging.LogManager;

import static org.hamcrest.Matchers.containsString;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

public abstract class AdminServletModuleTestCase {

    private static final String HOST = "127.0.0.1";
    private AsyncHttpClient httpClient = new AsyncHttpClient();

    private Server server;
    private int serverPort;

    private ObjectReader objectReader = new ObjectMapper().reader().withType(JsonNode.class);

    @Inject
    GuiceFilter filter;
    @Inject
    HealthCheckRegistry healthCheckRegistry;
    @Inject
    MetricRegistry metricRegistry;

    @Before
    public void setUp() throws Exception {
        LogManager.getLogManager().reset();
        SLF4JBridgeHandler.install();

        Guice.createInjector(Stage.PRODUCTION, getAdminServletModule()).injectMembers(this);

        // set up some metrics
        metricRegistry.timer("timer1");
        metricRegistry.register("gauge1", new Gauge<Integer>() {
            @Override
            public Integer getValue() {
                return 33;
            }
        });
        metricRegistry.counter("counter1").inc(74);
        metricRegistry.meter("meter1");
        metricRegistry.histogram("histogram1");

        healthCheckRegistry.register("hc1", new HealthCheck() {
            @Override
            protected Result check() throws Exception {
                return Result.healthy("yay");
            }
        });

        // spin up a jetty server

        server = new Server();
        ServerConnector connector = new ServerConnector(server);
        connector.setHost(HOST);

        // leave default port so it'll be random
        server.addConnector(connector);

        // servlet handler will contain the InvalidRequestServlet and the GuiceFilter
        ServletContextHandler servletHandler = new ServletContextHandler();
        servletHandler.setAttribute(HealthCheckServlet.HEALTH_CHECK_REGISTRY, healthCheckRegistry);
        servletHandler.setAttribute(MetricsServlet.METRICS_REGISTRY, metricRegistry);
        servletHandler.setContextPath("/");

        servletHandler.addServlet(new ServletHolder(new InvalidRequestServlet()), "/*");

        // add guice servlet filter
        FilterHolder guiceFilter = new FilterHolder(filter);
        servletHandler.addFilter(guiceFilter, "/*", EnumSet.allOf(DispatcherType.class));

        HandlerCollection handlerCollection = new HandlerCollection();
        handlerCollection.addHandler(servletHandler);

        server.setHandler(handlerCollection);

        server.start();
        serverPort = connector.getLocalPort();
        httpClient = new AsyncHttpClient();
    }

    @After
    public void tearDown() throws Exception {
        server.stop();
    }

    @Test
    public void testGetMetrics() throws InterruptedException, ExecutionException, IOException {
        Response response = get(getMetricsPath());

        assertEquals(200, response.getStatusCode());

        JsonNode expected = objectReader.readValue(this.getClass().getResourceAsStream("metricsResponse.json"));
        JsonNode actual = objectReader.readValue(response.getResponseBodyAsBytes());

        assertEquals(expected, actual);
    }

    @Test
    public void testGetPing() throws IOException, ExecutionException, InterruptedException {
        Response response = get(getPingPath());

        assertEquals(200, response.getStatusCode());
        assertEquals("pong\n", response.getResponseBody());
    }

    @Test
    public void testGetThreads() throws InterruptedException, ExecutionException, IOException {
        Response response = get(getThreadsPath());

        assertEquals(200, response.getStatusCode());

        // the sort of thing that a list of threads should contain
        assertThat(response.getResponseBody(), containsString("RUNNABLE"));
    }

    @Test
    public void testGetHealthCheck() throws InterruptedException, ExecutionException, IOException {
        Response response = get(getHealthCheckPath());

        assertEquals(200, response.getStatusCode());

        // the sort of thing that a list of threads should contain
        JsonNode expected = objectReader.readValue(this.getClass().getResourceAsStream("healthChecksResponse.json"));
        JsonNode actual = objectReader.readValue(response.getResponseBodyAsBytes());

        assertEquals(expected, actual);
    }

    protected abstract Module getAdminServletModule();

    protected abstract String getMetricsPath();

    protected abstract String getPingPath();

    protected abstract String getThreadsPath();

    protected abstract String getHealthCheckPath();

    private Response get(String path) throws InterruptedException, ExecutionException, IOException {
        return httpClient.prepareGet("http://" + HOST + ":" + serverPort + path)
            .execute(new ResponseCompletionHandler()).get();
    }

    private static class ResponseCompletionHandler extends AsyncCompletionHandler<Response> {
        @Override
        public Response onCompleted(Response response) throws Exception {
            return response;
        }
    }
}
