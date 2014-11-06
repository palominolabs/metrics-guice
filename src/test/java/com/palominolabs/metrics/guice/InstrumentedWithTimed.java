package com.palominolabs.metrics.guice;

import com.codahale.metrics.annotation.Timed;

public class InstrumentedWithTimed {
    @Timed(name = "things")
    public String doAThing() {
        return "poop";
    }

    @Timed
    String doAThingWithDefaultScope() {
        return "defaultResult";
    }

    @Timed
    protected String doAThingWithProtectedScope() {
        return "defaultProtected";
    }

    @Timed(name = "absoluteName", absolute = true)
    protected String doAThingWithAbsoluteName() {
        return "defaultProtected";
    }
}
