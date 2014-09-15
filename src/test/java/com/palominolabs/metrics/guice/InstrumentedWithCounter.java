package com.palominolabs.metrics.guice;

import com.codahale.metrics.annotation.Counted;

public class InstrumentedWithCounter {
    @Counted(name = "things")
    public String doAThing() {
        return "poop";
    }

    @Counted
    public String doAnotherThing() {
        return "anotherThing";
    }
    
    @Counted(name="absoluteName", absolute = true)
    public String doAThingWithAbsoluteName() {
        return "anotherThingWithAbsoluteName";
    }
}
