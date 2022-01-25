import cleanString from '../utils/cleanString'

describe('cleanString', () => {
  it('removes all extra white spaces between words in the query string', () => {
    const query = 'test string'
    expect(cleanString('test             string')).toEqual(query)
  })

  it('removes all extra white spaces at the end and beginning of the query string', () => {
    const query = 'test string'
    expect(cleanString('     test string               ')).toEqual(query)
  })
})
