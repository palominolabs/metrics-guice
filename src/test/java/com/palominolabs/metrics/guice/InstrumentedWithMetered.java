package com.palominolabs.metrics.guice;

import com.codahale.metrics.annotation.Metered;

public class InstrumentedWithMetered {
    @Metered(name = "things")
    public String doAThing() {
        return "poop";
    }

    @Metered
    String doAThingWithDefaultScope() {
        return "defaultResult";
    }

    @Metered
    protected String doAThingWithProtectedScope() {
        return "defaultProtected";
    }

    @Metered(name = "n")
    protected String doAThingWithName() {
        return "withName";
    }


    @Metered(name = "nameAbs", absolute = true)
    protected String doAThingWithAbsoluteName() {
        return "absoluteName";
    }
}
