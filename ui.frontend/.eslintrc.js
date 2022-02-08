module.exports = {
  plugins: ['sonarjs'],
  extends: [
    '@valtech-ch/eslint-config/config',
    '@valtech-ch/eslint-config/plugins',
    '@valtech-ch/eslint-config/rules',
    'plugin:sonarjs/recommended',
  ],
  parser: '@typescript-eslint/parser',
  parserOptions: {
    ecmaVersion: 2021, // Allows for the parsing of modern ECMAScript features
    sourceType: 'module', // Allows for the use of imports
    tsconfigRootDir: __dirname,
  },
  ignorePatterns: ['*.config.js', '.eslintrc.js'],
  rules: {
    'import/no-cycle': 'off',
  },
}
