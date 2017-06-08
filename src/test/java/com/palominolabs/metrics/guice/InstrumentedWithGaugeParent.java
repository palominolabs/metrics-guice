package com.palominolabs.metrics.guice;

import com.codahale.metrics.annotation.Gauge;

class InstrumentedWithGaugeParent {
    @Gauge(name = "gaugeParent", absolute = true)
    public String gaugeParent() {
        return "gaugeParent";
    }

    @Gauge
    public String justAGaugeFromParent() {
        return "justAGaugeFromParent";
    }

    @Gauge(name = "gaugeParentPrivate", absolute = true)
    private String gaugeParentPrivate() {
        return "gaugeParentPrivate";
    }
}
