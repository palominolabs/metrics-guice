package com.palominolabs.metrics.guice;

import com.codahale.metrics.annotation.Counted;

public class InstrumentedWithCounter extends InstrumentedWithCounterParent {
    @Counted(name = "things", monotonic = true)
    public String doAThing() {
        return "poop";
    }

    @Counted(monotonic = true)
    public String doAnotherThing() {
        return "anotherThing";
    }

    @Counted(monotonic = false)
    public String doYetAnotherThing() {
        return "anotherThing";
    }

    @Counted(name = "absoluteName", absolute = true, monotonic = true)
    public String doAThingWithAbsoluteName() {
        return "anotherThingWithAbsoluteName";
    }

}
