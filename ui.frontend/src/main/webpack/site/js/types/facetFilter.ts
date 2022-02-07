export interface FacetFilters {
  queryParameterName: string
  items: FacetItem[]
}

export interface FacetItem {
  filterFieldLabel: string
  filterFieldName: string
  filterFieldOptions: FilterFieldOption[]
}

export interface FilterFieldOption {
  value: string
  hits: number
}
