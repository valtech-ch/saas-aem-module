module.exports = {
  plugins: [
    '@typescript-eslint',
    'import',
    'simple-import-sort',
    'unused-imports',
    'sonarjs'
  ],
  extends: [
    // https://www.npmjs.com/package/eslint-config-airbnb-typescript
    'airbnb-typescript/base',

    // https://www.npmjs.com/package/@typescript-eslint/eslint-plugin
    'plugin:@typescript-eslint/eslint-recommended',
    'plugin:@typescript-eslint/recommended',
    'plugin:@typescript-eslint/recommended-requiring-type-checking',

    // https://www.npmjs.com/package/eslint-plugin-import
    'plugin:import/errors',
    'plugin:import/warnings',
    'plugin:import/typescript',
  
    'plugin:sonarjs/recommended',

    /**
     * Make sure to put prettier last, so it gets the chance to override other
     * configs.
    *
    * @see https://github.com/prettier/eslint-config-prettier
    */
   'prettier',
  ],
  parser: '@typescript-eslint/parser',
  parserOptions: {
    ecmaVersion: 2021, // Allows for the parsing of modern ECMAScript features
    sourceType: 'module', // Allows for the use of imports
    tsconfigRootDir: __dirname,
    project: ['./tsconfig.json']
  },
  ignorePatterns: ['*.config.js', '.eslintrc.js'],
  rules: {
    // general ESLint rules
    'arrow-body-style': 'off',
    'func-style': ['error', 'declaration', { allowArrowFunctions: true }],
    'no-console': 'warn',
    'no-use-before-define': 'off',

    // https://www.npmjs.com/package/@typescript-eslint/eslint-plugin
    '@typescript-eslint/no-use-before-define': 'off',
    '@typescript-eslint/no-unsafe-assignment': 'off',
    '@typescript-eslint/no-unsafe-call': 'off',
    '@typescript-eslint/no-unsafe-member-access': 'off',
    '@typescript-eslint/unbound-method': 'off',

    // https://www.npmjs.com/package/eslint-plugin-import
    'import/extensions': ['error', 'never'],
    'import/no-extraneous-dependencies': ['error', { devDependencies: true }],
    'import/prefer-default-export': 'off',

    // https://www.npmjs.com/package/eslint-plugin-simple-import-sort
    'simple-import-sort/imports': 'error',
    'simple-import-sort/exports': 'error',

    // https://www.npmjs.com/package/eslint-plugin-unused-imports
    'unused-imports/no-unused-imports': 'error',
    'import/no-cycle': 'off',
  }
}
