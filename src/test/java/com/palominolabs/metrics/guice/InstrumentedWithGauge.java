package com.palominolabs.metrics.guice;

import io.dropwizard.metrics5.annotation.Gauge;

@SuppressWarnings("UnusedReturnValue")
class InstrumentedWithGauge extends InstrumentedWithGaugeParent {
    @Gauge(name = "things")
    String doAThing() {
        return "poop";
    }

    @Gauge
    String doAnotherThing() {
        return "anotherThing";
    }

    @Gauge(name = "absoluteName", absolute = true)
    String doAThingWithAbsoluteName() {
        return "anotherThingWithAbsoluteName";
    }

    @Gauge(name = "gaugePrivate")
    private String gaugePrivate() {
        return "gaugePrivate";
    }
}
