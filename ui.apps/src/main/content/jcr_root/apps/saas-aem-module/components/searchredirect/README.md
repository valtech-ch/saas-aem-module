Search Redirect
====
Similar to the [Search](../search/README.md) component. It provides an input field that redirects the user to a search page.

## Features

### Use Object
The Search component uses the `com.valtech.aem.saas.api.fulltextsearch.SearchRedirectModel` Sling model as its Use-object.

### Behavior
The user enters search term in the text input field. While entering the search term, user is prompted with auto-complete
options. On submit of search term, the user is redirected to the search page, that is configured in the component's
dialog, with prefilled search term in the existing [Search](../search/README.md) component.

### Authoring
This component is intended to be used as part of a page header or in the content of an error handling page.

#### Edit Dialog Properties
The following properties are written to JCR for the Search component and are expected to be available as `Resource` properties:

1. `./searchPagePath` - Stores the path of the search page to be redirected to. (Required)
2. `./searchFieldPlaceholderText` - Defines the text to be displayed as placeholder in the input field. It has a default value defined as i18n entry.
