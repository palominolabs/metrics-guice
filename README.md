# Quick Start

### Get the artifacts

Maven:

```xml
<dependency>
  <groupId>com.palominolabs.metrics</groupId>
  <artifactId>metrics-guice</artifactId>
  <version>3.0.2</version>
</dependency>
```

Gradle:

```
compile 'com.palominolabs.metrics:metrics-guice:3.0.2'
```

### Install the Guice module

```java
// somewhere in your Guice module setup
install(new MetricsInstrumentationModule(yourFavoriteMetricRegistry));
```

### Use it

If you have a method like this:

```java
class SuperCriticalFunctionality {
    public void doSomethingImportant() {
        // critical business logic
    }
}
```

and you want to use a [Timer](http://metrics.codahale.com/manual/core/#timers) to measure duration, etc, you could always do it by hand:

```java
public void doSomethingImportant() {
    // timer is some Timer instance
    Timer.Context context = timer.time();
    try {
        // critical business logic
    } finally {
        context.stop();
    }
}
```

However, if you're instantiating that class with Guice, you could just do this:

```java
@Timed
public void doSomethingImportant() {
    // critical business logic
}
```

# History

This module started from the state of metrics-guice immediately before it was removed from the [main metrics repo](https://github.com/codahale/metrics) in [codahale/metrics@e058f76dabf3f805d1c220950a4f42c2ec605ecd](https://github.com/codahale/metrics/commit/e058f76dabf3f805d1c220950a4f42c2ec605ecd).

