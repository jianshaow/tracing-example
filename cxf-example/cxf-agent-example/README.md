# CXF Agent Example

Use opentracing agent (byteman) to inject tracing feature to CXF server and client.

```shell
mvn clean install

mvn jetty:run -Ddb_usr=<your_db_username> -Ddb_pwd=<your_db_password> -javaagent:target/agent/opentracing-agent.jar=sys:target/agent/cxf-opentracing-agent-rules.jar -Dorg.jboss.byteman.verbose
```
