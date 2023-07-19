/* eslint-disable no-void,@typescript-eslint/ban-ts-comment */

// eslint-disable-next-line @typescript-eslint/ban-types
function debounce<T extends Function>(
  func: T,
  timeout = 500,
) {
  let timer: ReturnType<typeof setTimeout>
  const callable = (...args: unknown[]) => {
    clearTimeout(timer)
    timer = setTimeout(() => {
      // @ts-ignore
      void func.apply(this, args)
    }, timeout)
  }

  return callable as unknown as T
}

export default debounce
