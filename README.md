# Poker application

This is the reference application for the poker game that is being developed as part of the agile software engineering training.

## Initial setup
- This repository is supposed to be forked for your training so that you can share code changes among your training team
- The code requires at least Java 8 and Maven 3 to compile and package
- If you use Eclipse as an IDE, import the project as "Existing Maven project"
- The project is a self contained Spring Boot project
- You can start the application either:
	- from within Eclipse; If you are using [Spring Tool Suite](https://spring.io/tools)
	- from the command line using `mvn spring-boot:run`
- The application will be available at http://localhost:8080
## Test support tools
- To continuously execute your tests you can use [Infinitest](http://infinitest.github.io/) which has plugins for Eclipse and IntelliJ
- To get code coverage statistics you can use:
	- IntelliJ's built-in [code coverage runner](https://www.jetbrains.com/help/idea/code-coverage.html)
	- [EclEmma](https://www.eclemma.org/) plugin for Eclipse
- Mutation tests are available using the PIT maven plugin.
	- Simply run `mvn org.pitest:pitest-maven:mutationCoverage` the report will be available at **target/pit-reports/{timestamp}/index.html**
	- NOTE: If pitest reports any problems you can try to run `mvn install` and then retry.
