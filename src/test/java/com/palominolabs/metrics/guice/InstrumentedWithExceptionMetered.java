package com.palominolabs.metrics.guice;

import com.codahale.metrics.annotation.ExceptionMetered;
import com.codahale.metrics.annotation.Metered;
import com.codahale.metrics.annotation.Timed;

@SuppressWarnings("UnusedReturnValue")
class InstrumentedWithExceptionMetered {

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

    @ExceptionMetered(name = "n")
    String explodeForMetricWithName() {
        throw new RuntimeException("Boom!");
    }

    @ExceptionMetered(name = "absoluteName", absolute = true)
    String explodeForMetricWithAbsoluteName() {
        throw new RuntimeException("Boom!");
    }

    @ExceptionMetered
    String explodeWithDefaultScope() {
        throw new RuntimeException("Boom!");
    }

    @ExceptionMetered
    String explodeWithProtectedScope() {
        throw new RuntimeException("Boom!");
    }

    @ExceptionMetered(name = "failures", cause = MyException.class)
    void errorProneMethod(RuntimeException e) {
        throw e;
    }

    @ExceptionMetered(name = "things",
        cause = ArrayIndexOutOfBoundsException.class)
    Object causeAnOutOfBoundsException() {
        @SuppressWarnings("MismatchedReadAndWriteOfArray")
        final Object[] arr = {};
        //noinspection ConstantConditions
        return arr[1];
    }

    @Timed
    @ExceptionMetered
    void timedAndException(RuntimeException e) {
        if (e != null) {
            throw e;
        }
    }

    @Metered
    @ExceptionMetered
    void meteredAndException(RuntimeException e) {
        if (e != null) {
            throw e;
        }
    }
}
