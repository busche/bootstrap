
= Bootstrap build instructions

== Maven

type

	mvn deploy

to run a complete cycle.

== Gradle

type

	gradle uploadArchives

to run a complete cycle.

Note:

* Copy gradle.properties.sample to gradle.properties and adjust username, password and maybe proxy information according to your current client set up. (The deployment server is configured directly in the build file)
