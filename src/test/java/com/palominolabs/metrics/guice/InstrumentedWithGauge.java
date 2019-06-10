package com.palominolabs.metrics.guice;

import com.codahale.metrics.annotation.Gauge;

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
