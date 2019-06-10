package com.palominolabs.metrics.guice;

import com.codahale.metrics.annotation.Counted;

@SuppressWarnings("UnusedReturnValue")
class InstrumentedWithCounterParent {

    @Counted(name = "counterParent", absolute = true)
    String counterParent() {
        return "counterParent";
    }
}
