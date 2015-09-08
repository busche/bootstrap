
= Bootstrap build instructions

== Ant

type 

	ant
	
and follow the instructions (error messages) to get the project up and running.

== Maven

type

	mvn deploy

to run a complete cycle.

Note:

* You need to define the repository '''bcs-repository''' along with username/password information in your ~/.m2/settings.xml file in order to actually deploy the artifact.

== Gradle

type

	gradle uploadArchives

to run a complete cycle.

Note:

* Copy gradle.properties.sample to gradle.properties and adjust username, password and maybe proxy information according to your current client set up. (The deployment server is configured directly in the build file)
