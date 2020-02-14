# Tracing Example

Research tracing solution on multiple scenarios.

## Prerequisite

The exampls need following service:

* Redis server

* Mysql server

Database information as below:

Schema: mydb; User: appuser; Password: 123456;

~~~sql
CREATE TABLE `mydb`.`application` (
  `id` INT NOT NULL,
  `name` VARCHAR(45) NULL,
  PRIMARY KEY (`id`)
);
~~~

* Cassandra server
Database information as below:

Keyspace: mydb;

~~~cql
CREATE TABLE application (
   id int,
   name text,
   PRIMARY KEY (id)
);
~~~

* Jaeger server

## Execution

```shell
mvn clean install

cd <example path>
mvn jetty:run -Ddb_usr=<your_db_username> -Ddb_pwd=<your_db_password>
```
The startup approach above is not for [cxf-agent-example](cxf-example/cxf-agent-example), [spring-agent-example](spring-example/spring-agent-example), [webapp-agent-example](webapp-example/webapp-agent-example), [spring-boot-agent-example](spring-example/spring-boot-agent-example), and [spring-boot-jaeger-example](spring-example/spring-boot-jaeger-example), they have their own special startup parameters.
