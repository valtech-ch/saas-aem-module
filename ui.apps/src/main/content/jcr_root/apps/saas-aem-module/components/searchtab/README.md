Search Tab
====
Component that renders the markup as soon as the search is performed. It displays search results.

## Features

### Use Object
The Search Tab component uses the `com.valtech.aem.saas.api.fulltextsearch.SearchTabModel` Sling model as its Use-object.

### Behavior
On submit of entered search term the children search tab components perform a search query and display the results. If
facets are configured, it will also display search results filtering options.

### Authoring
This is a component that is placed in the responsive-grid of the [Search](../search/README.md) component.

#### Edit Dialog Properties
The following properties are written to JCR for the Search component and are expected to be available as `Resource` properties:

1. `./title` - Will store the text of the title to be rendered on the top of the component. (Required)
2. `./facets` - Defines list of (label, index field name). This enables filtering of the search results by index fields.
3. `./filters` - Defines list of simple search filter entries. The filter entries are joined with a logical 'AND'
   operator.
4. `./template` - Defines SaaS query template name.

### Query parameters

| Name | Description | Example |
| --- | :--- | :--- |
| q | Full text query | q=foo bar |
| page | Results page to be shown. First page is set as **page=1** | page=2 |
| facetFilter | Search filter from selected facet options | facetFilter=domain:www.valtech.com&facetFilter=contentType:pdf,xml |
