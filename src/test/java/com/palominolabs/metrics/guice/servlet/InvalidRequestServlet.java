package com.palominolabs.metrics.guice.servlet;

/*
 * Copyright (c) 2012 Palomino Labs, Inc.
 */

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Jetty wants at least one servlet always, so we'll give it a very simple one...
 */
final class InvalidRequestServlet extends HttpServlet {

    private static final Logger logger = LoggerFactory.getLogger(InvalidRequestServlet.class);

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        logger.warn("Handled request: " + req.getPathInfo());
        resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
        resp.setContentType("text/plain");
        resp.setContentType("UTF-8");
        resp.getWriter().append("404");
    }
}
