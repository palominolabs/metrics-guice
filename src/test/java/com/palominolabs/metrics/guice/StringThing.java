package com.palominolabs.metrics.guice;

import com.codahale.metrics.annotation.Counted;

class StringThing extends GenericThing<String> {

    @Override
    @Counted(name = "stringThing", absolute = true, monotonic = true)
    void doThing(String s) {
    }
}
