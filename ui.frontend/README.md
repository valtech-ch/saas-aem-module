# Frontend Library of the AEM Module to integrate Valtech's Search as a Service

## Introduction
SAAS (Search as a Service) is continuously improved by Valtech Switzerland in cooperation with its clients. This service allows Valtech clients to jointly benefit from a constantly evolving and improved search service for digital experience platforms. Improvements are continuously rolled out as updates become available without any downtime. The offering consists of the following main components:

* SAAS Crawler - Web page crawling, extraction of relevant content areas, and metadata.
* SAAS Administration UI - Interface for controlling and configuring the crawlers.
* SAAS Engine - Collection of APIs for full text or typeahead content queries.
* SAAS AEM Search Component - this very component for integrating SAAS into AEM within a very short timeframe through configuration and styling

## Approach
The AEM Search Component can be installed and connected to SAAS through configuration. Content Authors then configure content pages that act as Search Result Pages (SERP). The author configures the behaviour like filters or tabs according to the specific needs of the SERP.

On top of that, AEM developers can extend the AEM Search Component to implement custom requirements that are not covered out-of-the-box. The component follows the architectural patterns of Adobe Core Component. Therefore, the approach is known and straight forward to extend while ensuring maintainability of the core module.

## Build and release procedure

1. `npm version major | minor | patch` (depending on the changes that need to be released)
2. `npm run package`
3. `npm run release` (while being logged in to npm and member of the `valtech-ch` organisation)
