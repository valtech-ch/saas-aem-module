# AEM Server Setup

By default AEM is expected to listen on localhost on port 4502. This setting can be overridden by adding parameters:

* -Daem.port=4502
* -Daem.host=localhost
* -Daem.publish.port=4503
* -Daem.publish.host=localhost

You need AEM 6.5 with service pack 10 or AEMaaCS.

# Modules

The main parts of the template are:

* api: Java bundle that exports the interface that are used for consumption of SaaS api.
* core: Java bundle containing all core functionality like OSGi services, listeners or schedulers, as well as
  component-related Java code such as servlets or request filters.
* it.tests: Java based integration tests
* ui.apps: contains the /apps (and /etc) parts of the project, ie JS&CSS clientlibs, components, and templates
* ui.content: contains sample content using the components from the ui.apps
* ui.config: contains runmode specific OSGi configs for the project
* ui.frontend: contains the FE implementation of search components (markup and js)
* all: a single content package that embeds all of the compiled modules (bundles and content packages) including any
  vendor dependencies
* analyse: this module runs analysis on the project which provides additional validation for deploying into AEMaaCS

# Build and Deploy

To build and deploy run this in the base folder:

```bash
mvn clean install -PautoInstallPackage
```

To build all the modules and deploy the `all` package to a local instance of AEM, run in the project root directory the
following command:

```bash
mvn clean install -PautoInstallSinglePackage
```

Or to deploy it to a publish instance, run

```bash
mvn clean install -PautoInstallSinglePackagePublish
```

Or alternatively

```bash
mvn clean install -PautoInstallSinglePackage -Daem.port=4503
```

To deploy only a bundle to the author, run

```bash
mvn clean install -PautoInstallBundle
```

Or on publish

```bash
mvn clean install -PautoInstallBundle Daem.port=4503
```

To deploy only a single content package, run in the sub-module directory (i.e `ui.apps`)

```bash
mvn clean install -PautoInstallPackage
```

Or on publish:

```bash
mvn clean install -PautoInstallPackagePublish
```

# ClientLibs

The [frontend module](../ui.frontend/README.md) is made available using
an [AEM ClientLib](https://helpx.adobe.com/experience-manager/6-5/sites/developing/using/clientlibs.html). When
executing the NPM build script, the app is built and
the [`aem-clientlib-generator`](https://github.com/wcm-io-frontend/aem-clientlib-generator) package takes the resulting
build output and transforms it into such a ClientLib.

A ClientLib will consist of the following files and directories:

- `css/`: CSS files which can be requested in the HTML
- `css.txt` (tells AEM the order and names of files in `css/` so they can be merged)
- `js/`: JavaScript files which can be requested in the HTML
- `js.txt` (tells AEM the order and names of files in `js/` so they can be merged
- `resources/`: Source maps, non-entrypoint code chunks (resulting from code splitting), static assets (e.g. icons),
  etc.

# Styling recommendation

wknd-sample clientlib was created in order to demonstrate and provide a structure when it comes to implementing design for Search as Service. Please check [here](../ui.frontend/src/main/webpack/site/styles/wkndsample/README.md)
# Testing

There are three levels of testing contained in the project:

## Unit tests

This show-cases classic unit testing of the code contained in the bundle. To test, execute:

    mvn clean test

## Integration tests

This allows running integration tests that exercise the capabilities of AEM via HTTP calls to its API. To run the
integration tests, run:

    mvn clean verify -Plocal

Test classes must be saved in the `src/main/java` directory (or any of its subdirectories), and must be contained in
files matching the pattern `*IT.java`.

The configuration provides sensible defaults for a typical local installation of AEM. If you want to point the
integration tests to different AEM author and publish instances, you can use the following system properties via
Maven's `-D`
flag.

| Property | Description | Default value |
| --- | --- | --- |
| `it.author.url` | URL of the author instance | `http://localhost:4502` |
| `it.author.user` | Admin user for the author instance | `admin` |
| `it.author.password` | Password of the admin user for the author instance | `admin` |
| `it.publish.url` | URL of the publish instance | `http://localhost:4503` |
| `it.publish.user` | Admin user for the publish instance | `admin` |
| `it.publish.password` | Password of the admin user for the publish instance | `admin` |

The integration tests in this project use the [AEM Testing Clients](https://github.com/adobe/aem-testing-clients) and
showcase some recommended [best practices](https://github.com/adobe/aem-testing-clients/wiki/Best-practices) to be put
in use when writing integration tests for AEM.

# Static Analysis

The `analyse` module performs static analysis on the project for deploying into AEMaaCS. It is automatically run when
executing

    mvn clean install

from the project root directory. Additional information about this analysis and how to further configure it can be found
here https://github.com/adobe/aemanalyser-maven-plugin

# Maven settings

The project comes with the auto-public repository configured. To setup the repository in your Maven settings, refer to:

    http://helpx.adobe.com/experience-manager/kb/SetUpTheAdobeMavenRepository.html

# SonarCloud integration

In order to scan code to detect bugs, vulnerabilities and code smells we integrated this project with SonarCloud. The
GitHub action for Sonarcloud analysis runs each time when there is a Pull Request created or commit to main branch.

## Example of pull request analysis

<img src="../images/SonarCloud-analysis-in-Checks.png" alt="PR analysis example">

# Code Formatting

Please use our standard code formatters for [IntelliJ](formatter/intellij-saas-aem.xml).
