import cleanString from '../utils/cleanString'

describe('cleanString', () => {
  const query = 'test string'
  it('removes all extra white spaces between words in the query string', () => {
    expect(cleanString('test             string')).toEqual(query)
  })

  it('removes all extra white spaces at the end and beginning of the query string', () => {
    expect(cleanString('     test string               ')).toEqual(query)
  })
})
