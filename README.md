# Tracing Example

Research tracing solution on multiple scenarios, especially on legacy framework.

```shell
mvn clean install

cd <example path>
mvn jetty:run -Ddb_usr=<your_db_username> -Ddb_pwd=<your_db_password>
```
The startup approach above is not for [cxf-agent-example](cxf-example/cxf-agent-example), [cxf-agent-lightweight-example](cxf-example/cxf-agent-lightweight-example), and [spring-boot-jaeger-example](spring-example/spring-boot-jaeger-example), they have their own special startup parameters.
