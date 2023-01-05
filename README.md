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
mvn jetty:run-forked
```
