export const STORAGE_SUGGESTIONS_KEY = '__saas__suggestions__'
export const STORAGE_QUERY_STRING_KEY = '__saas__queryString__'

export const cleanSessionStorage = (
  sessionStorageKeys = [STORAGE_SUGGESTIONS_KEY, STORAGE_QUERY_STRING_KEY],
): void => {
  sessionStorageKeys?.forEach((storageKey: string) =>
    sessionStorage.removeItem(storageKey),
  )
}

export const getSessionStorage = ({
  storageKey,
  defaultValue,
  parse = true,
}: {
  storageKey: string
  defaultValue: string
  parse?: boolean
}) =>
  parse
    ? JSON.parse(sessionStorage.getItem(storageKey) || defaultValue)
    : sessionStorage.getItem(storageKey) || defaultValue

export const setSessionStorage = ({
  storageKey,
  data,
  stringify = true,
}: {
  storageKey: string
  data: [] | Record<string, unknown> | string
  stringify?: boolean
}): void => {
  sessionStorage.setItem(
    storageKey,
    stringify ? JSON.stringify(data as string) : (data as string),
  )
}
