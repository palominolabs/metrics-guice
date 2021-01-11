package com.palominolabs.metrics.guice;

import io.dropwizard.metrics5.annotation.Counted;

@SuppressWarnings("UnusedReturnValue")
class InstrumentedWithCounterParent {

    @Counted(name = "counterParent", absolute = true)
    String counterParent() {
        return "counterParent";
    }
}
