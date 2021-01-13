package com.palominolabs.metrics.guice;

import io.dropwizard.metrics5.annotation.Counted;

class StringThing extends GenericThing<String> {

    @Override
    @Counted(name = "stringThing", absolute = true, monotonic = true)
    void doThing(String s) {
    }
}
