package com.palominolabs.metrics.guice;

import com.codahale.metrics.annotation.Counted;

public class InstrumentedWithCounterParent {

    @Counted(name = "counterParent", absolute = true)
    public String counterParent() {
        return "counterParent";
    }
}
