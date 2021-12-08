module.exports = {
  plugins: ['sonarjs'],
  extends: [
    '@valtech-ch/eslint-config/config',
    '@valtech-ch/eslint-config/plugins',
    '@valtech-ch/eslint-config/rules',
    'plugin:sonarjs/recommended',
  ],
  parserOptions: {
    ecmaVersion: 2021, // Allows for the parsing of modern ECMAScript features
    sourceType: 'module', // Allows for the use of imports
  },
  rules: {
    'import/no-cycle': 'off',
  },
}
