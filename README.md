# Quick Start

### Get the artifacts

Maven:

```xml
<dependency>
  <groupId>com.palominolabs.metrics</groupId>
  <artifactId>metrics-guice</artifactId>
  <version>[the latest version]</version>
</dependency>
```

Gradle:

```
compile 'com.palominolabs.metrics:metrics-guice:[the latest version]'
```

### Install the Guice module

```java
// somewhere in your Guice module setup
install(new MetricsInstrumentationModule(yourFavoriteMetricRegistry));
```

### Use it

The `MetricsInstrumentationModule` you installed above will create and appropriately invoke a [Timer](https://dropwizard.github.io/metrics/3.1.0/manual/core/#timers) for `@Timed` methods, a [Meter](https://dropwizard.github.io/metrics/3.1.0/manual/core/#meters) for `@Metered` methods, a [Counter](https://dropwizard.github.io/metrics/3.1.0/manual/core/#counters) for `@Counted` methods, and a [Gauge](https://dropwizard.github.io/metrics/3.1.0/manual/core/#gauges) for `@Gauge` methods. `@ExcptionMetered` is also supported; this creates a `Meter` that measures how often a method throws exceptions.

The annotations have some configuration options available for metric name, etc. You can also provide a custom `MetricNamer` implementation if the default name scheme does not work for you.

#### Example

If you have a method like this:

```java
class SuperCriticalFunctionality {
    public void doSomethingImportant() {
        // critical business logic
    }
}
```

and you want to use a [Timer](https://dropwizard.github.io/metrics/3.1.0/manual/core/#timers) to measure duration, etc, you could always do it by hand:

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

This module started from the state of metrics-guice immediately before it was removed from the [main metrics repo](https://github.com/dropwizard/metrics) in [dropwizard/metrics@e058f76dabf3f805d1c220950a4f42c2ec605ecd](https://github.com/dropwizard/metrics/commit/e058f76dabf3f805d1c220950a4f42c2ec605ecd).

