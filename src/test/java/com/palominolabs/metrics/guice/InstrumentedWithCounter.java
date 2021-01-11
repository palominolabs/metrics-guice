package com.palominolabs.metrics.guice;

import io.dropwizard.metrics5.annotation.Counted;

@SuppressWarnings("UnusedReturnValue")
class InstrumentedWithCounter extends InstrumentedWithCounterParent {
    @Counted(name = "things", monotonic = true)
    String doAThing() {
        return "poop";
    }

    @Counted(monotonic = true)
    String doAnotherThing() {
        return "anotherThing";
    }

    @SuppressWarnings("DefaultAnnotationParam")
    @Counted(monotonic = false)
    String doYetAnotherThing() {
        return "anotherThing";
    }

    @Counted(name = "absoluteName", absolute = true, monotonic = true)
    String doAThingWithAbsoluteName() {
        return "anotherThingWithAbsoluteName";
    }

}
