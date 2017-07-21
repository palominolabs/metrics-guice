[![Build Status](https://semaphoreci.com/api/v1/marshallpierce/metrics-guice/branches/master/badge.svg)](https://semaphoreci.com/marshallpierce/metrics-guice)
 [ ![Download](https://api.bintray.com/packages/marshallpierce/maven/com.palominolabs.metrics%3Ametrics-guice/images/download.svg) ](https://bintray.com/marshallpierce/maven/com.palominolabs.metrics%3Ametrics-guice/_latestVersion) 
# Quick Start

### Get the artifacts

Artifacts are released in [Bintray](https://bintray.com/). For gradle, use the `jcenter()` repository. For maven, [go here](https://bintray.com/bintray/jcenter?filterByPkgName=com.palominolabs.metrics%3Ametrics-guice) and click "Set me up".

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
install(MetricsInstrumentationModule.builder().withMetricRegistry(yourFavoriteMetricRegistry).build());
```

### Use it

The `MetricsInstrumentationModule` you installed above will create and appropriately invoke a [Timer](https://dropwizard.github.io/metrics/3.1.0/manual/core/#timers) for `@Timed` methods, a [Meter](https://dropwizard.github.io/metrics/3.1.0/manual/core/#meters) for `@Metered` methods, a [Counter](https://dropwizard.github.io/metrics/3.1.0/manual/core/#counters) for `@Counted` methods, and a [Gauge](https://dropwizard.github.io/metrics/3.1.0/manual/core/#gauges) for `@Gauge` methods. `@ExceptionMetered` is also supported; this creates a `Meter` that measures how often a method throws exceptions.

The annotations have some configuration options available for metric name, etc. You can also provide a custom `MetricNamer` implementation if the default name scheme does not work for you.

### Customizing annotation lookup

By default `MetricsInstrumentationModule` will provide metrics only for annotated methods. You can also look for annotations on the enclosing classes, or both, or provide your own custom logic.  To change annotation resolution, provide an `AnnotationResolver` when building the `MetricsInstrumentationModule`. `MethodAnnotationResolver` is the default implementation. `ClassAnnotationResolver` will look for annotations on the class instead of the method. You can invoke multiple resolvers in order with `ListAnnotationResolver `, so if you wanted to look in methods first and then the class, you could do that:

```java
// somewhere in your Guice module setup
install(
    MetricsInstrumentationModule.builder()
        .withMetricRegistry(yourFavoriteMetricRegistry)
        .withAnnotationResolver(new ListAnnotationResolver(Lists.newArrayList(new ClassAnnotationResolver(), new MethodAnnotationResolver()))
        .build()
);
```

### Metric namer

The default `MetricNamer` implementation probably does what you want out of the box, but you can also write and use your own. 

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

### Limitations

Since this uses Guice AOP, instances must be created by Guice; see [the Guice wiki](https://github.com/google/guice/wiki/AOP). This means that using a Provider where you create the instance won't work, or binding a singleton to an instance, etc.

Guice AOP doesn't allow us to intercept method calls to annotated methods in supertypes, so `@Counted`, etc, will not have metrics generated for them if they are in supertypes of the injectable class. One small consolation is that `@Gauge` methods can be anywhere in the type hierarchy since they work differently from the other metrics (the generated Gauge object invokes the `java.lang.reflect.Method` directly, so we can call the supertype method unambiguously).

One common way users might hit this issue is if when trying to use `@Counted`, etc on a JAX-RS resource annotated with `@Path`, `@GET`, etc. This may pose problems for the JAX-RS implementation because the thing it has at runtime is now an auto-generated proxy class, not the "normal" class. A perfectly reasonable approach is instead to handle metrics generation for those classes via the hooks available in the JAX-RS implementation. For Jersey 2, might I suggest [jersey2-metrics](https://bitbucket.org/marshallpierce/jersey2-metrics)?

# History

This module started from the state of metrics-guice immediately before it was removed from the [main metrics repo](https://github.com/dropwizard/metrics) in [dropwizard/metrics@e058f76dabf3f805d1c220950a4f42c2ec605ecd](https://github.com/dropwizard/metrics/commit/e058f76dabf3f805d1c220950a4f42c2ec605ecd).

