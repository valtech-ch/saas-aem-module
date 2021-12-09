# AEM Module to integrate Valtech's Search as a Service

[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=valtech-ch_saas-aem-module&metric=alert_status)](https://sonarcloud.io/dashboard?id=valtech-ch_saas-aem-module)

# Table of contents

- [Introduction](#introduction)
- [Approach](#approach)
- [Requirements](#requirements)
- [Installation](#installation)
  - [Full](#full)
    - [Clientlibs](#clientlibs)
  - [Api with default implementation](#api-with-default-implementation)
  - [Uninstall](#uninstall)
- [AEM APIs](#aem-apis)
- [Configurations](#configurations)
  - [OSGi configurations](#osgi-configurations)
  - [Context Aware configurations](#context-aware-configurations)
- [Components](#components)
  - [New approach](#new-approach)
  - [Available components](#available-components)
    - [Search](#search)
    - [Search Tab](#search-tab)
    - [Search Redirect](#search-redirect)
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

* saas.dependencies
* saas.site

Add them as entries in the multifield _**Client Libraries JavaScript Page Head**_, in the page policy for the editable
template where the components would be used.

## Api with default implementation

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

| Interface | Default implementation |
| --- | ---: |
|[FulltextSearchService](api/src/main/java/com/valtech/aem/saas/api/fulltextsearch/FulltextSearchService.java) | [DefaultFulltextSearchService](core/src/main/java/com/valtech/aem/saas/core/fulltextsearch/DefaultFulltextSearchService.java) |
|[TypeaheadService](api/src/main/java/com/valtech/aem/saas/api/typeahead/TypeaheadService.java) | [DefaultTypeaheadService](core/src/main/java/com/valtech/aem/saas/core/typeahead/DefaultTypeaheadService.java) |
|[BestBetsService](api/src/main/java/com/valtech/aem/saas/api/bestbets/BestBetsService.java) | [DefaultBestBetsService](core/src/main/java/com/valtech/aem/saas/core/bestbets/DefaultBestBetsService.java) |
|[IndexUpdateService](api/src/main/java/com/valtech/aem/saas/api/indexing/IndexUpdateService.java) | [DefaultIndexUpdateService](core/src/main/java/com/valtech/aem/saas/core/indexing/DefaultIndexUpdateService.java) |

# Configurations

Compared to V1 module, the configurations are refactored. Redundant config fields are extracted in a single
configuration service. Some configurations, that could/should change depending on the resource's context, are moved in
Context Aware configurations.

## OSGi configurations

1. [Search as a Service - Search Service HTTP Connection Configuration](http://localhost:4502/system/console/configMgr/Search%20as%20a%20Service%20-%20Search%20Service%20Connection%20Configuration%20Service)
2. [Search as a Service - Fulltext Search Service Configuration](http://localhost:4502/system/console/configMgr/Search%20as%20a%20Service%20-%20Fulltext%20Search%20Service)
3. [Search as a Service - Typeahead Service Configuration](http://localhost:4502/system/console/configMgr/Search%20as%20a%20Service%20-%20Typeahead%20Service)
4. [Search as a Service - Best Bets Service Configuration](http://localhost:4502/system/console/configMgr/Search%20as%20a%20Service%20-%20Best%20Bets%20Service)
5. [Search as a Service - Index Update Service Configuration](http://localhost:4502/system/console/configMgr/Search%20as%20a%20Service%20-%20Index%20Update%20Service)

## Context Aware configurations

| Label | Name | Description | Required |
| --- | :--- | :--- | :---:|
| Search Index | index | Index defined in SaaS admin | x |
| Search Client | client | Client identifier defined in SaaS admin | x |
| Search Project Id | projectId | Project identifier defined in SaaS admin | x |
| Search Filters | searchFilters | Key/value pairs of **field name** and **value**
| Search Templates | templates | List of custom query templates' names, defined in SaaS admin, for specialized/different field boosting strategies. (Optional)
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

* Search Redirect
* Search
* Search Tab

**Component group:** _Search as a Service - Content_

### Search

This is a container that accepts Search Tab components.

#### Purpose

To integrate search input and search results in a page.

#### Dialog

The component can be configured with:

* Title - text that is displayed on the top of the component (it is optional)
* Language - defines the language of the search results (it is optional, it overrides the context language)
* Search field placeholder text (it is optional. it has a default value defined as i18n entry.)
* Number of results per page (it is optional. it overrides the default value of 10 results per page)
* Search Filter entries

#### Usage

This component can be included on any page (fix or inside a parsys/responsive-grid). By default, it includes one Search
Tab component.

### Search Tab

This is a component that is placed in the responsive-grid of the Search component.

#### Purpose

To execute search query and display results in a tab separated/navigated container.

#### Dialog

The component can be configured with:

* Title - Text that is used as the tab label; It is required;
* Facets - Defines list of (label, index field name). This enables filtering of the search results by index fields.
* Search Filter entries

#### Usage

This component is exclusively placed inside a Search component's responsive grid. The configuration fields that the
component offers, enable the author to define filter entries per tab.

#### Query parameters

| Name | Description | Example |
| --- | :--- | :--- |
| q | Full text query | q=foo bar |
| page | Results page to be shown. First page is set as **page=1** | page=2 |
| facetFilter | Search filter from selected facet options | facetFilter=domain:www.valtech.com&facetFilter=contentType:pdf,xml |

### Search Redirect

#### Purpose

To integrate easily accessible search input field that redirects the user to the search page. This component is intended
to be used as part of a page header or in the content of an error handling page.

#### Dialog

The component can be configured with:

* Search page path - the location of the search page (required)
* Search field placeholder text - overrides the placeholder text from search component found on the above configured
  search page

#### Usage

This component should be utilized to redirect the user to the page where the search component is placed. It offers a
search input with autocomplete feature. With submission of the search term, the user is redirected to a search page
displaying the according search results.

# License

The AECU tool is licensed under the [MIT LICENSE](LICENSE).

# Developers

See our [developer zone](docs/developers.md).
