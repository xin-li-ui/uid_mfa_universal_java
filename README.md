# UID MFA Universal Prompt Java Client

[![Issues](https://img.shields.io/github/issues/xin-li-ui/uid_mfa_universal_java)](https://github.com/xin-li-ui/uid_mfa_universal_java/issues)
[![Forks](https://img.shields.io/github/forks/xin-li-ui/uid_mfa_universal_java)](https://github.com/xin-li-ui/uid_mfa_universal_java/network/members)
[![Stars](https://img.shields.io/github/stars/xin-li-ui/uid_mfa_universal_java)](https://github.com/xin-li-ui/uid_mfa_universal_java/stargazers)
[![License](https://img.shields.io/badge/License-View%20License-orange)](https://github.com/xin-li-ui/uid_mfa_universal_java/blob/master/LICENSE)


This library allows a web developer to quickly add UID's interactive, self-service, multi-factor authentication to any Java web login form.

See our developer documentation at https://ubiquiti.atlassian.net/l/cp/FSZ71zmi for guidance on integrating UID MFA into your web application.

What's here:
* `uid-mfa-universal-sdk` - The UID SDK for interacting with the UID Universal Prompt
* `uid-mfa-example` - An example web application with UID integrated

# Usage
This library requires Java 17 or later and uses Maven to build the JAR files.

Run `mvn package` to generate a JAR with dependencies, suitable for inclusion in a web application.

The UID Universal Client for Java is available from UID Security on Maven.  Include the following in your dependency definitions:
```
<!-- https://mvnrepository.com/artifact/com.uidsecurity/uid-mfa-universal-sdk -->
<dependency>
    <groupId>com.uidsecurity</groupId>
    <artifactId>uid-mfa-universal-sdk</artifactId>
    <version>1.0.0</version>
</dependency>
```
See https://mvnrepository.com/artifact/com.uidsecurity/uid-mfa-universal-sdk/1.0.0 for more details.

# Demo

## Build

From the root directory run:

`mvn clean install`

## Run

In order to run this project, ensure the values in `application.yaml` are filled out with the values
from the UID Manager Portal (clientId, clientSecret, apiHost, and redirectUri)

From the root of the `uid-mfa-example` project run the following to start the server:
`mvn spring-boot:run`

Navigate to <http://localhost:8080> to see a mock user login form.  Enter username and any password to initiate UID MFA.

# Testing

From the root directory run:

`mvn test`

# Linting

From the root directory run:

`mvn checkstyle:check`

# Support

Please report any bugs, feature requests, or issues to us directly at support@uidsecurity.com.

Thank you for using UID!

http://www.uidsecurity.com/
