Gatling plugin for SBT - Scala demo project
===========================================

A simple showcase of an SBT project using the Gatling plugin for SBT. Refer to the plugin documentation
[on the Gatling website](https://gatling.io/docs/current/extensions/sbt_plugin/) for usage.

This project is written in Scala. For other languages, consider using
[our other supported build plugins](https://gatling.io/docs/gatling/reference/current/extensions/).

Minimal `build.sbt`, requiring [SBT 1](https://www.scala-sbt.org/download.html)

Sample [Simulation](https://gatling.io/docs/gatling/reference/current/general/concepts/#simulation) class,
  demonstrating sufficient Gatling functionality

Run
---------
```bash
$ sbt clean compile
$ sudo PROFILER=enable sbt gatling:test
```
