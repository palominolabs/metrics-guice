package com.palominolabs.metrics.guice.tests;

import com.codahale.metrics.annotation.ExceptionMetered;
import com.codahale.metrics.annotation.Metered;
import com.codahale.metrics.annotation.Timed;

public class InstrumentedWithExceptionMetered {

    @ExceptionMetered(name = "exceptionCounter")
    String explodeWithPublicScope(boolean explode) {
        if (explode) {
            throw new RuntimeException("Boom!");
        } else {
            return "calm";
        }
    }

    @ExceptionMetered
    String explodeForUnnamedMetric() {
        throw new RuntimeException("Boom!");
    }
    
    @ExceptionMetered(name="n")
    String explodeForMetricWithName() {
        throw new RuntimeException("Boom!");
    }

    @ExceptionMetered(name="absoluteName", absolute = true)
    String explodeForMetricWithAbsoluteName() {
        throw new RuntimeException("Boom!");
    }

    @ExceptionMetered
    String explodeWithDefaultScope() {
        throw new RuntimeException("Boom!");
    }

    @ExceptionMetered
    protected String explodeWithProtectedScope() {
        throw new RuntimeException("Boom!");
    }

    @ExceptionMetered(name = "failures", cause = MyException.class)
    public void errorProneMethod(RuntimeException e) {
        throw e;
    }

    @ExceptionMetered(name = "things",
                      cause = ArrayIndexOutOfBoundsException.class)
    public Object causeAnOutOfBoundsException() {
        final Object[] arr = {};
        return arr[1];
    }

    @Timed
    @ExceptionMetered
    public void timedAndException(RuntimeException e) {
        if (e != null) {
            throw e;
        }
    }

    @Metered
    @ExceptionMetered
    public void meteredAndException(RuntimeException e) {
        if (e != null) {
            throw e;
        }
    }
}
