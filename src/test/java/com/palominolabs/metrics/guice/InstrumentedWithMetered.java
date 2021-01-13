package com.palominolabs.metrics.guice;

import io.dropwizard.metrics5.annotation.Metered;

@SuppressWarnings({"UnusedReturnValue", "WeakerAccess"})
class InstrumentedWithMetered {
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
