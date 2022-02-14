# WKND sample styleguide

The information provided here aims at showing how to structure and implement your styles. However, feel free to organize the way that fits your project.

## Table of Contents

- [Files](#files)
- [Classes](#classes)

## Files

Use the following files in order to update according to your UI needs. Filenames are self-explanatory.

- \_animation.scss
- \_breakpoints.scss
- \_colors.scss
- \_media_queries.scss
- \_spacing.scss
- \_typography.scss

They are all imported in \_wkndsample.scss.

## Classes

Below are all the classes that are used in the SAAS module and can be styled.

### Form and search input

```
.cmp-saas__form {}
.cmp-saas__search-input-wrapper {}
.cmp-saas__search-input {}
```

### Results container

```
.cmp-saas__results {}
.cmp-saas__results--show {}
.cmp-saas__results--hide {}
```

### Results items container

```
.cmp-saas__results-items {}
```

### Results items

```
.cmp-saas__results-item {}
.cmp-saas__results-item--best-best {}
.cmp-saas__results-item-description {}
.cmp-saas__results-item-title {}
.cmp-saas__results-item-url {}
.cmp-saas__results-item-link-text {}
```

### Search tab

```
.cmp-saas__tab {}
```

### Search, load more and reset buttons

```
.cmp-saas__button {}
.cmp-saas__load-more-button {}
.cmp-saas__search-reset-button {}
```

### Autosuggest

For when query is mispelled. The application will offer alternatives words.

```
cmp-saas__autosuggest {}
```

### Not found result

```
cmp-saas__not-found {}
```

### Typeahead suggestion

```
#cmp-saas-suggestions {}
.cmp-saas__suggestions-element {}
.cmp-saas__suggestions-element--active {}
.cmp-saas__suggestions-element--matched-query {}
```

### Facets groups

```
.cmp-sass__facets-groups {}
.cmp-sass__facets-group {}
.cmp-sass__facets-group-title {}
```

### Facet

```
.cmp-sass__facet {}
.cmp-sass__facet-label {}
.cmp-sass__facet--no-result {}
```
