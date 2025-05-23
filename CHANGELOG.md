# Changelog

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [3.0.3] - 2025-05-09

* Release with JReleaser
* Move from Nexus2 to Maven Central
* Remove support before Java 11

## [3.0.2] - 2024-11-04

* Support for Quarkus `3.16.x`

## [3.0.1] - 2023-05-09

* Support for the new API of `BeanContainer` (https://github.com/quarkusio/quarkus/issues/35744)
* Upgrade dependencies
* Remove project jflu-event-store
* Remove the use of JLink

## [3.0.0] - 2023-05-09

* Support for Quarkus `3.x.x`

## [1.1.4] - 2023-02-23

* Support for Quarkus `2.16.3`

## [1.1.3] - 2022-11-03

* Support for consuming `overridenEmitter` attributes

## [1.1.2] - 2022-08-16

* Bootstrapping a Quarkus RabbitMQ Subscription allows the redefinition of properties

## [1.1.1] - 2022-08-12

* Quarkus RabbitMQ Subscription is initialized before the StartupEvent

## [1.1.0] - 2022-08-11

* Stable Quarkus extension
* Native support for GraalVM

## [1.0.13] - 2022-08-11

* Increase compatibility with Quarkus native image build (producer and subscriber)

## [1.0.12] - 2022-08-10

* Include Jandex files in artifacts
* Increase Quarkus Beans discoverability
* Use Gradle to build the Quarkus extensions
* Extension for RabbitMQ Subscription (`RabbitMQSubscriptionBootstraper`)

## [1.0.8] - 2022-08-09

* Support for Quarkus `2.11.2`
* Support for Quarkus native image build (producer only)

## [1.0.7] - 2021-11-06

* Support RabbitMQ with SSL

## [1.0.6] - 2021-10-04

* Support for the attribute `requestMetadata` on events of type `entity_change`  

## [1.0.5] - 2021-08-31

* Update jackson to avoid CVE-2020-25649 

## [1.0.4] - 2021-08-30

* Parsing of Event: convenient support for `LocalDate`

## [1.0.3] - 2021-03-04

* Listener: Override attributes when reading them from the environment variables
* Listener: Delete a queue
* Listener: Stop a consumer

## [1.0.2] - 2021-02-26

* Allow users to define a RabbitMQExceptionHandler without using reflection
* Support for Quarkus `1.12.0.Final`

## [1.0.1] - 2020-04-01

* Support for Quarkus `1.3.1.Final`

## [1.0.0] - 2020-02-01

* Flag release as stable
* Support for Quarkus `1.2.0.Final`
* Expose RabbitMQ's client

## [0.14.3] - 2020-01-10

* JPMS bugfix

## [0.14.2] - 2020-01-10

* Opens package to configure the Exception Handler by reflection

## [0.14.1] - 2020-01-09

* Add new properties on Quarkus extension: `enabled`

## [0.14.0] - 2020-01-09

* Support for JPMS
* Upgrade RabbitMQ client to `5.8.0`

## [0.13.7] - 2019-11-18

* Quarkus extension
* Timestamp is always produced in UTC
* Move Gradle Wrapper to `6.0`
* Upgrade RabbitMQ client to `5.7.3`

## [0.13.6] - 2019-09-11

* Support for `EventMappingKind.TRIGGERED_JOB` (woops)

## [0.13.5] - 2019-09-11

* Support for `EventKind.TRIGGERED_JOB`

## [0.13.4] - 2019-08-01

* Convenient methods on `EntityChange` (breaking change: Use of `java.util.Optional`)
* Build with JDK12
* Move Gradle Wrapper to `5.5.1`

## [0.13.3] - 2019-07-31

* Convenient methods on `EntityChange`

## [0.13.2] - 2019-07-26

* Better support for null values in polymorphic associations

## [0.13.1] - 2019-07-25

* Support both snake case and camel case when reading polymorphic associations

## [0.13.0] - 2019-07-25

* Support for polymorphic associations on `EntityData`