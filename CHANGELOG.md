# Changelog

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

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