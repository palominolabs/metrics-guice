package com.palominolabs.metrics.guice;

import com.codahale.metrics.annotation.Timed;

@SuppressWarnings({"UnusedReturnValue", "WeakerAccess"})
class InstrumentedWithTimed {
    @Timed(name = "things")
    public String doAThing() throws InterruptedException {
        Thread.sleep(10);
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
