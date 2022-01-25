export const STORAGE_SUGGESTIONS_KEY = '__saas__suggestions__'
export const STORAGE_QUERY_STRING_KEY = '__saas__queryString__'

export const cleanSessionStorage = (
  sessionStorageKeys = [STORAGE_SUGGESTIONS_KEY, STORAGE_QUERY_STRING_KEY],
): void => {
  sessionStorageKeys?.forEach((storageKey: string) =>
    sessionStorage.removeItem(storageKey),
  )
}
