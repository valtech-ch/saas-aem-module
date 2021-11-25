/* eslint-disable no-void,@typescript-eslint/ban-ts-comment */

function debounce(
  func: (...args: any[]) => void | Promise<void>,
  timeout = 500,
) {
  let timer: ReturnType<typeof setTimeout>
  return (...args: any[]) => {
    clearTimeout(timer)
    timer = setTimeout(() => {
      // @ts-ignore
      void func.apply(this, args)
    }, timeout)
  }
}

export default debounce
