# Frontend Library of the AEM Module to integrate Valtech's Search as a Service

This is the frontend library of the [AEM Module to integrate Valtech's Search as a Service](https://github.com/valtech-ch/saas-aem-module/blob/main/README.md).
More info about the project can be also found there.

## Integration

The frontend sources can be either embedded with clientlibs or added as a dependency in your frontend build.
To use with clientlibs, please see the details outlined [here](https://github.com/valtech-ch/saas-aem-module/blob/main/README.md#clientlibs).

## Build and release procedure

1. `npm version major | minor | patch` (depending on the changes that need to be released)
2. `npm run package`
3. `npm run release` (while being logged in to npm and member of the `valtech-ch` organisation)
