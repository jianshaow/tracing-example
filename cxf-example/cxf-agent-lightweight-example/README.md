# CXF Agent Lightweight Example

Use opentracing agent (byteman) to intercept cxf client to create span. no-intrusive solution.

```shell
mvn clean install

mvn jetty:run -Ddb_usr=<your_db_username> -Ddb_pwd=<your_db_password> -javaagent:target/agent/opentracing-agent.jar=sys:target/agent/cxf-opentracing-agent-lightweight-rules.jar,sys:target/agent/opentracing-agent-rules-java-web-servlet-filter.jar -Dorg.jboss.byteman.verbose
```
