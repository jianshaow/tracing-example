# Webapp Agent Example

Use OpenTelemetry agent to inject tracing feature transparently.

```shell
mvn clean install

mvn jetty:run -Djetty.deployMode=FORK
```

Stop jetty with command below in another terminal

```shell
mvn jetty:stop
```