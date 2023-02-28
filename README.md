# AEM Module to integrate Valtech's Search as a Service

[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=valtech-ch_saas-aem-module&metric=alert_status)](https://sonarcloud.io/dashboard?id=valtech-ch_saas-aem-module)
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/io.github.valtech-ch/saas-aem.all/badge.svg)](https://maven-badges.herokuapp.com/maven-central/io.github.valtech-ch/saas-aem.all)
[![javadoc](https://javadoc.io/badge2/io.github.valtech-ch/saas-aem.api/javadoc.svg)](https://javadoc.io/doc/io.github.valtech-ch/saas-aem.api)

AEM Search Module to use and customize Valtech's Search as a Service (SAAS) offering within AEM.

![typeahead](images/searchpage-typeahead.png)

- [About Search as a Service](#about-search-as-a-service)
- [AEM Search Module](#aem-search-module)
- [Features](#features)
  - [Components](#components)
  - [AEM APIs](#aem-apis)
  - [Configurations](#configurations)
    - [OSGi configurations](#osgi-configurations)
    - [Context Aware configurations](#context-aware-configurations)
  - [Rendering](#rendering)
- [System Requirements](#system-requirements)
- [Installation](#installation)
  - [Clientlibs](#clientlibs)
  - [Embedding in a maven project](#embedding-in-a-maven-project)
    - [Step 1: Add SaaS as a dependency](#step-1-add-saas-as-a-dependency)
    - [Step 2: Add SaaS as an embed package](#step-2-add-saas-as-an-embed-package)
  - [Uninstall](#uninstall)
- [License](#license)
- [Developers](#developers)

# About Search as a Service

Search as a Service (SAAS) is continuously improved by Valtech Switzerland in cooperation with its clients. This service
allows Valtech clients to jointly benefit from a constantly evolving and improved search service for digital experience
platforms. Improvements are continuously rolled out as updates become available without any downtime. The offering
consists of the following main components:

* SAAS Crawler - Web page crawling, extraction of relevant content areas, and metadata.
* SAAS Administration UI - Interface for controlling and configuring the crawlers.
* SAAS Engine - Collection of APIs for full text or typeahead content queries.
* SAAS AEM Module - this module has been designed to easily integrate SAAS into AEM within a very short timeframe through
  configuration and styling. On top, it offers an API, either for backend or frontend customizations.

# AEM Search Module

The AEM Search Module can be installed and connected to SAAS through configuration. Content Authors then configure
content pages that act as Search Result Pages (SERP). The author configures the behaviour like filters or tabs according
to the specific needs of the SERP.

On top of that, AEM developers can extend the AEM Search Component to implement custom requirements that are not covered
out-of-the-box. The component follows the architectural patterns of [AEM WCM Core Components](https://github.com/adobe/aem-core-wcm-components) . Therefore, the approach is
known and straight forward to extend while ensuring maintainability of the core module. In the SAAS Administration UI
the SAAS Crawler can be configured. A recommended approach is to use Sitemaps (although raw crawling works as well) in
order to indicate which pages should be shown. To generate Sitemaps, the Apache Sling Sitemap module
can be used described on the AEM
documentation: https://experienceleague.adobe.com/docs/experience-manager-cloud-service/overview/seo-and-url-management.html?lang=en#building-an-xml-sitemap-on-aem

Metadata such as a title, description, publication date, taxonomy and more can be scraped from the HTML or Sitemaps in order to allow the usage of advanced features such as Facets, Filters or customized sorting.   

# Features

- **Production-Ready:** Ready made APIs and components that are well tested and used in Production.
- **Cloud-Ready:** Whether on AEM as a Cloud Service, on Adobe Managed Services, or on-premise, they just work.
- **Customizable:** Modules are designed to be customized: API module with interfaces, components can be changed similar to AEM core components, modules published on maven central and npm registry.  
- **Themeable:** The components markup follows BEM CSS conventions and are prefixed to avoid any integration conflicts.
- **Configurable:** Features can be configured on component and on context aware-level.
- **WebApp-Ready:** The streamlined JSON output allows client-side rendering.
- **Developer friendly:** Intensive documentation in readme files, javadocs and sample code.
- **Open Sourced:** If something is not as it should, contribution is welcomed.

## Components

1. **[Search:](ui.apps/src/main/content/jcr_root/apps/saas-aem-module/components/search/README.md)** Container component which consists of an input field for the search and Search Tabs. Typically used on a Search Result Page   
2. **[Search Tab:](ui.apps/src/main/content/jcr_root/apps/saas-aem-module/components/searchtab/README.md)** Component which performs a search and displays the Search Results. 
3. **[Search Redirect:](ui.apps/src/main/content/jcr_root/apps/saas-aem-module/components/searchredirect/README.md)** Component usually added within the Header of the page. Consists of an input field with autocomplete which redirects the user to a Search Result Page on click 

**Component group:** _Search as a Service - Content_

## AEM APIs

AEM APIs are interfaces exported by `saas-aem.api` module, that provide consumption of SaaS REST APIs.
The `saas-aem.core` module offers a default implementation for each of these interfaces. However, client projects
utilizing the module have the possibility to provide a custom implementation by specifying a higher `service.ranking`
property and implementing the interface accordingly.

| Interface                                                                                                     |                                                                                     Default implementation in `saas-aem.core` |
|---------------------------------------------------------------------------------------------------------------|------------------------------------------------------------------------------------------------------------------------------:|
| [FulltextSearchService](api/src/main/java/com/valtech/aem/saas/api/fulltextsearch/FulltextSearchService.java) | [DefaultFulltextSearchService](core/src/main/java/com/valtech/aem/saas/core/fulltextsearch/DefaultFulltextSearchService.java) |
| [TypeaheadService](api/src/main/java/com/valtech/aem/saas/api/autocomplete/TypeaheadService.java)             |                [DefaultTypeaheadService](core/src/main/java/com/valtech/aem/saas/core/typeahead/DefaultTypeaheadService.java) |
| [BestBetsService](api/src/main/java/com/valtech/aem/saas/api/bestbets/BestBetsService.java)                   |                   [DefaultBestBetsService](core/src/main/java/com/valtech/aem/saas/core/bestbets/DefaultBestBetsService.java) |
| [IndexUpdateService](api/src/main/java/com/valtech/aem/saas/api/indexing/IndexUpdateService.java)             |             [DefaultIndexUpdateService](core/src/main/java/com/valtech/aem/saas/core/indexing/DefaultIndexUpdateService.java) |
| [TrackingService](api/src/main/java/com/valtech/aem/saas/api/tracking/TrackingService.java)                   |                   [DefaultTrackingService](core/src/main/java/com/valtech/aem/saas/core/tracking/DefaultTrackingService.java) |

## Configurations

Configurations are split in OSGi and Context-Aware.

### OSGi configurations

1. [Search as a Service - Search Api Request Executor Service Configuration](http://localhost:4502/system/console/configMgr/com.valtech.aem.saas.core.http.client.DefaultSearchApiRequestExecutorService)
2. [Search as a Service - Search Admin Request Executor Service Configuration](http://localhost:4502/system/console/configMgr/com.valtech.aem.saas.core.http.client.DefaultSearchAdminRequestExecutorService)
3. [Search as a Service - Search Service HTTP Connection Configuration](http://localhost:4502/system/console/configMgr/com.valtech.aem.saas.core.http.client.DefaultSearchServiceConnectionConfigurationService)
4. [Search as a Service - Fulltext Search Service Configuration](http://localhost:4502/system/console/configMgr/com.valtech.aem.saas.core.fulltextsearch.DefaultFulltextSearchService)
5. [Search as a Service - Autocomplete Service Configuration](http://localhost:4502/system/console/configMgr/com.valtech.aem.saas.core.autocomplete.DefaultAutocompleteService)
6. [Search as a Service - Best Bets Service Configuration](http://localhost:4502/system/console/configMgr/com.valtech.aem.saas.core.bestbets.DefaultBestBetsService)
7. [Search as a Service - Index Update Service Configuration](http://localhost:4502/system/console/configMgr/com.valtech.aem.saas.core.indexing.DefaultIndexUpdateService)

### Context Aware configurations

| Label                              | Name                           | Description                                                                                                                | Required |
|------------------------------------|:-------------------------------|:---------------------------------------------------------------------------------------------------------------------------|:--------:|
| Search Index                       | index                          | Index defined in SaaS admin                                                                                                |    x     |
| Search Project Id                  | projectId                      | Project identifier defined in SaaS admin                                                                                   |    x     |
| Search Filters                     | searchFilters                  | Key/value pairs of **field name** and **value**                                                                            |          |
| Search result highlight tag name   | highlightTagName               | The name of the tag that will be used to highlight portions of text in the search results. (Optional; Default value: `em`) |          |
| Enable Best Bets                   | enableBestBets                 | Flag that enables displaying best bets on the top of the search results. Defaults to `false`                               |          |
| Enable Auto Suggest                | enableAutoSuggest              | Flag that enables auto suggest feature in the search component. Defaults to `true`                                         |          |
| Enable Autocomplete                | enableAutocomplete             | Flag that enables autocomplete feature in the search input. Defaults to `true`                                             |          |
| Autocomplete Trigger Threshold     | autocompleteTriggerThreshold   | The minimum number of search input characters required for displaying autocomplete options.                                |          |
| Autocomplete options Max Limit     | autocompleteOptionsMax         | The maximum number of autocomplete options displayed.                                                                      |          |
| Enable Search Result Item Tracking | enableSearchResultItemTracking | Flag that enables search result items tracking. Defaults to `false`                                                        |          |

![Context aware configuration](images/context-aware_search-configuration.png "Search configuration")

## Rendering

As the search components are highly dynamic, rendering is  completely done in the frontend.
Within the HTL only a webcomponent with the api-endpoint and configuration is exported.
The components are utilizing `org.apache.sling.models.jacksonexporter` to export the
sling model into json. The exported json is then consumed by the FE and the actual component markup is generated.

## Clientlibs

To use the Search components, the following client libraries should be included:

* [saas-aem-module.base](https://github.com/valtech-ch/saas-aem-module/blob/main/ui.frontend/clientlib.config.js#L67)

Add them as entries in the multifield _**Client Libraries JavaScript Page Head**_, in the page policy for the editable
template where the components would be used.

To see a styling sample based on WKND theme, the following client library should be included (NOT RECOMMENDED FOR PRODUCTION):

* [saas-aem-module.wknd.sample](https://github.com/valtech-ch/saas-aem-module/blob/main/ui.frontend/clientlib.config.js#L82)

To read more information about how to style, [here](ui.frontend/src/main/webpack/site/styles/wkndsample/README.md)

# Installation

As a **prerequisite** you need to be onboarded and get an API token and access to the Search Admin Interfaces.

We are happy to do a live demo and provide all necessary information.
Please reach out to [contact.ch@valtech.com](mailto:contact.ch@valtech.com) for licensing costs of the service and more information.

You can download the crx-package from the [release page](https://github.com/valtech-ch/saas-aem-module/releases/latest).

## System Requirements

| AEM 6.5 | AEM as a Cloud Service | JDK | Maven |
| --- | --- | --- | --- |
| 6.5.10.0+ (*) | Continual | 8, 11 | 3.3.9+ |

## Embedding in a maven project

To embed it as part of a deployment you can find the packages and bundles on [Maven Central](https://repo1.maven.org/maven2/io/github/valtech-ch/saas-aem/)

If your project has similar structure to an aem archetype generated project, then update the pom.xml of your
project's **all** module. Whether you choose all modules or partial modules, add the appropriate
dependencies and configure the **filevault-package-maven-plugin** plugin accordingly to embedd them.

### Step 1: Add SaaS as a dependency

In the <dependencies> section of your all project’s pom.xml file, add this:

```xml

<dependency>
  <groupId>io.github.valtech-ch</groupId>
  <artifactId>saas-aem.all</artifactId>
  <version>${saas.version}</version>
</dependency>
```

### Step 2: Add SaaS as an embed package

In the filevault-package-maven-plugin plugin configuration of your all project’s pom.xml file, add this:

```xml

<embeddeds>
  <embedded>
    <groupId>io.github.valtech-ch</groupId>
    <artifactId>saas-aem.all</artifactId>
    <type>zip</type>
    <target>/apps/vendor-packages/container/install</target>
  </embedded>
  ...
</embeddeds>
```

## Uninstall

To uninstall the module, delete the following subtrees:

* /apps/saas-aem-module
* /apps/saas-aem-module-packages
* /home/users/system/saas

## Getting started

After installation of the module following steps need to be done:

### OSGI Configurations
* Configure [Search as a Service - Search Api Request Executor Service Configuration](http://localhost:4502/system/console/configMgr/com.valtech.aem.saas.core.http.client.DefaultSearchApiRequestExecutorService):
  * Base URL
  * JWT Token
* Configure [Search as a Service - Search Admin Request Executor Service Configuration](http://localhost:4502/system/console/configMgr/com.valtech.aem.saas.core.http.client.DefaultSearchAdminRequestExecutorService)
  * Base URL
  * JWT Token or Basic Authentication 

Within AEM as a Cloud Service Base URL and JWT Token can be configured as a [secret environment variable](https://experienceleague.adobe.com/docs/experience-manager-cloud-service/content/implementing/deploying/configuring-osgi.html?lang=en#secret-configuration-values) 

### Context-aware configurations

The following fields are mandatory: SaaS Index

### Sample content

To get started right away you can install the sample [ui.content](https://github.com/valtech-ch/saas-aem-module/releases/latest) via CRX Package Manager and start [exploring](http://localhost:4502/editor.html/content/saas-aem-module/us/en/search-page.html).
Optionally you can install the [wcm io Context-Aware Configuration Editor](https://wcm.io/caconfig/editor/usage.html) in order to see the context aware configuration within AEM.  

### Embed component in custom templates

Obviously you can embed the components into your own pages and templates.

1. Open Template editor of the custom template
2. Open Page Policies and
* include JS client library **saas-aem-module.base** in Client Libraries JavaScript Page Head
* optionally add client library **saas-aem-module.wknd.sample** to have sample styling. This part you want to customize (see [Clientlibs](#clientlibs))
  ![open page policies](images/open-page-policies.png) ![clientlibs page policies](images/clientlibs-page-policies.png)
3. Open Policy of the responsive grid and include component group Search as a Service - Content in allowed components list.
![clientlibs content policies](images/clientlibs-content-policies.png)
 
Afterwards you can drag & drop a search component onto the defined page and start exploring.

# License

The SaaS AEM module is licensed under the [MIT LICENSE](LICENSE).

# Developers

See our [developer zone](docs/developers.md).
