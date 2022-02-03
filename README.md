# AEM Module to integrate Valtech's Search as a Service

[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=valtech-ch_saas-aem-module&metric=alert_status)](https://sonarcloud.io/dashboard?id=valtech-ch_saas-aem-module)

# Table of contents

- [Introduction](#introduction)
- [Approach](#approach)
- [Requirements](#requirements)
- [Installation](#installation)
  - [Full](#full)
  - [Api with default implementation and osgi configurations](#api-with-default-implementation-and-osgi-configurations)
  - [Uninstall](#uninstall)
- [AEM APIs](#aem-apis)
- [Configurations](#configurations)
  - [OSGi configurations](#osgi-configurations)
  - [Context Aware configurations](#context-aware-configurations)
- [Components](#components)
  - [New approach](#new-approach)
  - [Available components](#available-components)
- [License](#license)
- [Developers](#developers)

# Introduction

SAAS (Search as a Service) is continuously improved by Valtech Switzerland in cooperation with its clients. This service
allows Valtech clients to jointly benefit from a constantly evolving and improved search service for digital experience
platforms. Improvements are continuously rolled out as updates become available without any downtime. The offering
consists of the following main components:

* SAAS Crawler - Web page crawling, extraction of relevant content areas, and metadata.
* SAAS Administration UI - Interface for controlling and configuring the crawlers.
* SAAS Engine - Collection of APIs for full text or typeahead content queries.
* SAAS AEM Search Component - this very component for integrating SAAS into AEM within a very short timeframe through
  configuration and styling

# Approach

The AEM Search Component can be installed and connected to SAAS through configuration. Content Authors then configure
content pages that act as Search Result Pages (SERP). The author configures the behaviour like filters or tabs according
to the specific needs of the SERP.

On top of that, AEM developers can extend the AEM Search Component to implement custom requirements that are not covered
out-of-the-box. The component follows the architectural patterns of Adobe Core Component. Therefore, the approach is
known and straight forward to extend while ensuring maintainability of the core module. In the SAAS Administration UI
the SAAS Crawler can be configured. A recommended approach is to use Sitemaps (although raw crawling works as well) in
order to indicate which pages should be shown in the Search Admin. To generate Sitemaps, the Apache Sling Sitemap module
can be used described on the AEM
documentation: https://experienceleague.adobe.com/docs/experience-manager-cloud-service/overview/seo-and-url-management.html?lang=en#building-an-xml-sitemap-on-aem

<a name="requirements"></a>

# Requirements

| AEM | JDK |
| --- | --- |
| 6.5 / Cloud | 8, 11 |

# Installation

You can download the bundles from [Maven Central](https://repo1.maven.org/maven2/io/github/valtech-ch/saas-aem/).

## Full

```xml

<dependency>
  <groupId>io.github.valtech-ch</groupId>
  <artifactId>saas-aem.all</artifactId>
  <version>${project.version}</version>
</dependency>
```

### Clientlibs

To use the OOTB components, the following client libraries should be included:

* [saas-aem-module.base](ui.apps/src/main/content/jcr_root/apps/saas-aem-module/clientlibs/clientlib-base)

Add them as entries in the multifield _**Client Libraries JavaScript Page Head**_, in the page policy for the editable
template where the components would be used.

## Partial (w/o AEM components)

```xml

<dependency>
  <groupId>io.github.valtech-ch</groupId>
  <artifactId>saas-aem.api</artifactId>
  <version>${project.version}</version>
</dependency>

<dependency>
  <groupId>io.github.valtech-ch</groupId>
  <artifactId>saas-aem.core</artifactId>
  <version>${project.version}</version>
</dependency>

<dependency>
  <groupId>io.github.valtech-ch</groupId>
  <artifactId>saas-aem.ui.config</artifactId>
  <version>${project.version}</version>
</dependency>
```

## Uninstall

To uninstall the module, delete the following subtrees:

* /apps/saas-aem-module
* /apps/saas-aem-module-packages
* /home/users/system/saas

# AEM APIs

Aem APIs are interfaces exported by `saas-aem.api` module, that provide consumption of SaaS REST APIs.
The `saas-aem.core` module offers a default implementation for each of these interfaces. However, the client project,
utilizing this module, has the possibility to provide a custom implementation by specifying a higher `service.ranking`
property.

| Interface | Default implementation in `saas-aem.core` |
| --- | ---: |
|[FulltextSearchService](api/src/main/java/com/valtech/aem/saas/api/fulltextsearch/FulltextSearchService.java) | [DefaultFulltextSearchService](core/src/main/java/com/valtech/aem/saas/core/fulltextsearch/DefaultFulltextSearchService.java) |
|[TypeaheadService](api/src/main/java/com/valtech/aem/saas/api/autocomplete/TypeaheadService.java) | [DefaultTypeaheadService](core/src/main/java/com/valtech/aem/saas/core/typeahead/DefaultTypeaheadService.java) |
|[BestBetsService](api/src/main/java/com/valtech/aem/saas/api/bestbets/BestBetsService.java) | [DefaultBestBetsService](core/src/main/java/com/valtech/aem/saas/core/bestbets/DefaultBestBetsService.java) |
|[IndexUpdateService](api/src/main/java/com/valtech/aem/saas/api/indexing/IndexUpdateService.java) | [DefaultIndexUpdateService](core/src/main/java/com/valtech/aem/saas/core/indexing/DefaultIndexUpdateService.java) |

# Configurations

Configurations are split in OSGi and Context-Aware.

## OSGi configurations

1. [Search as a Service - Search Service HTTP Connection Configuration](http://localhost:4502/system/console/configMgr/com.valtech.aem.saas.core.http.client.DefaultSearchServiceConnectionConfigurationService)
2. [Search as a Service - Fulltext Search Service Configuration](http://localhost:4502/system/console/configMgr/com.valtech.aem.saas.core.fulltextsearch.DefaultFulltextSearchService)
3. [Search as a Service - Autocomplete Service Configuration](http://localhost:4502/system/console/configMgr/com.valtech.aem.saas.core.autocomplete.DefaultAutocompleteService)
4. [Search as a Service - Best Bets Service Configuration](http://localhost:4502/system/console/configMgr/com.valtech.aem.saas.core.bestbets.DefaultBestBetsService)
5. [Search as a Service - Index Update Service Configuration](http://localhost:4502/system/console/configMgr/com.valtech.aem.saas.core.indexing.DefaultIndexUpdateService)
6. [Search as a Service - Search Admin Request Executor Service Configuration](http://localhost:4502/system/console/configMgr/com.valtech.aem.saas.core.http.client.DefaultSearchAdminRequestExecutorService)
7. [Search as a Service - Search Api Request Executor Service Configuration](http://localhost:4502/system/console/configMgr/com.valtech.aem.saas.core.http.client.DefaultSearchApiRequestExecutorService)

## Context Aware configurations

| Label | Name | Description | Required |
| --- | :--- | :--- | :---:|
| Search Index | index | Index defined in SaaS admin | x |
| Search Client | client | Client identifier defined in SaaS admin | x |
| Search Project Id | projectId | Project identifier defined in SaaS admin | x |
| Search Filters | searchFilters | Key/value pairs of **field name** and **value**
| Search result highlight tag name | highlightTagName | The name of the tag that will be used to highlight portions of text in the search results. (Optional; Default value: `em`)
| Enable Best Bets | enableBestBets | Flag that enables displaying best bets on the top of the search results. Defaults to `false`
| Enable Auto Suggest | enableAutoSuggest | Flag that enables auto suggest feature in the search component. Defaults to `true`

# Components

## New approach

Conceptually what differs from the standard way of developing aem components (with HTL) is that the markup rendering is
now moved in the `ui.frontend` module, and is dynamically generated with javascript. The HTL script is only rendering a
placeholder in **wcmmode = edit**. The components are utilizing `org.apache.sling.models.jacksonexporter` to export the
sling model into json. The exported json is then consumed by the FE and the actual component markup is generated.

## Available components

* [Search Redirect](ui.apps/src/main/content/jcr_root/apps/saas-aem-module/components/searchredirect/README.md)
* [Search](ui.apps/src/main/content/jcr_root/apps/saas-aem-module/components/search/README.md)
* [Search Tab](ui.apps/src/main/content/jcr_root/apps/saas-aem-module/components/searchtab/README.md)

**Component group:** _Search as a Service - Content_

# License

The SaaS AEM tool is licensed under the [MIT LICENSE](LICENSE).

# Developers

See our [developer zone](docs/developers.md).
