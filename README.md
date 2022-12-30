# Tracing Example

Research tracing solution on multiple scenarios.

## Prerequisite

The exampls need following service:

* Redis server

* Mysql server

Database information as below:

Schema: mydb; User: appuser; Password: 123456;

~~~sql
CREATE SCHEMA mydb;
CREATE USER appuser;
SET PASSWORD FOR 'appuser'@'%'=password('123456');
GRANT ALL PRIVILEGES ON mydb.* TO 'appuser'@'%';

CREATE TABLE `mydb`.`application` (
  `id` INT NOT NULL,
  `name` VARCHAR(45) NULL,
  PRIMARY KEY (`id`)
);

INSERT INTO application VALUES (1, 'test');
~~~

* Cassandra server
Database information as below:

Keyspace: mydb;

~~~cql
CREATE KEYSPACE mydb WITH replication = {'class': 'SimpleStrategy'}

CREATE TABLE mydb.application (
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
mvn jetty:run -Ddb_usr=appuser -Ddb_pwd=123456
```
The startup approach above is not for [cxf-agent-example](cxf-example/cxf-agent-example), [spring-agent-example](spring-example/spring-agent-example), [webapp-agent-example](webapp-example/webapp-agent-example), [spring-boot-agent-example](spring-example/spring-boot-agent-example), and [spring-boot-jaeger-example](spring-example/spring-boot-jaeger-example), they have their own special startup parameters.
