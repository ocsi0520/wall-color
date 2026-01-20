// @ts-check
const eslint = require('@eslint/js');
const eslintPluginImport = require('eslint-plugin-import');
const { defineConfig } = require('eslint/config');
const tseslint = require('typescript-eslint');
const angular = require('angular-eslint');

module.exports = defineConfig([
  {
    files: ['**/*.ts'],
    extends: [
      eslint.configs.recommended,
      tseslint.configs.recommended,
      tseslint.configs.stylistic,
      angular.configs.tsRecommended,
      eslintPluginImport.flatConfigs.typescript,
    ],
    processor: angular.processInlineTemplates,
    rules: {
      '@angular-eslint/directive-selector': [
        'error',
        {
          type: 'attribute',
          prefix: 'app',
          style: 'camelCase',
        },
      ],
      '@angular-eslint/component-selector': [
        'error',
        {
          type: 'element',
          prefix: 'app',
          style: 'kebab-case',
        },
      ],
      '@typescript-eslint/explicit-member-accessibility': [
        'error',
        {
          overrides: { constructors: 'no-public' },
        },
      ],
      '@typescript-eslint/explicit-function-return-type': [
        'error',
        {
          allowExpressions: false,
          allowTypedFunctionExpressions: true,
          allowHigherOrderFunctions: false,
          allowDirectConstAssertionInArrowFunctions: true,
          allowConciseArrowFunctionExpressionsStartingWithVoid: false,
          allowFunctionsWithoutTypeParameters: false,
          allowedNames: ['args'],
          allowIIFEs: false,
        },
      ],
      // prettier does the job: https://stackoverflow.com/a/58977894/11009933
      indent: 'off',
      complexity: ['error', 8],
      'max-len': ['error', 120],
      'max-params': ['error', 3],
      'max-depth': ['error', 2],
      'max-nested-callbacks': ['error', 1],
      'max-statements': ['error', 8],
      'max-lines': ['error', { max: 200, skipBlankLines: true, skipComments: true }],
      'max-lines-per-function': ['error', { max: 20, skipBlankLines: true, skipComments: true }],
      '@typescript-eslint/member-ordering': 'error',
      'linebreak-style': ['error', 'unix'],
      quotes: ['error', 'single', { avoidEscape: true }],
      semi: ['error', 'always'],

      'no-unused-vars': 'off',
      '@typescript-eslint/no-unused-vars': ['error', { argsIgnorePattern: '^_' }],
      'import/no-unresolved': 'off',
      'import/no-absolute-path': 'off',
      'import/extensions': 'off',
      'no-plusplus': 'off',
      'lines-between-class-members': [
        'error',
        {
          enforce: [
            { blankLine: 'always', prev: '*', next: 'method' },
            { blankLine: 'never', prev: 'field', next: 'field' },
          ],
        },
      ],
      'no-useless-constructor': 'off',
      'no-empty-function': ['error', { allow: ['constructors'] }],
      'wc/guard-super-call': 'off',
      'no-param-reassign': ['error', { props: false }],
      '@typescript-eslint/no-shadow': 'error',
      'no-shadow': 'off',
      'default-case': 'off',
      'class-methods-use-this': 'off',
      // for overload we need this, and anyway ts takes care of this rule
      // https://eslint.org/docs/latest/rules/no-dupe-class-members#handled_by_typescript
      'no-dupe-class-members': 'off',
      'dot-notation': 'off',
      'import/no-extraneous-dependencies': ['error', { devDependencies: true }],
      // https://eslint.org/docs/latest/rules/no-undef#handled_by_typescript
      'no-undef': 'off',
    },
  },
  {
    files: ['**/*.test.ts', '**/*.spec.ts', '**/*.mock.ts'],
    rules: {
      'max-nested-callbacks': ['error', 4],
      'max-lines-per-function': 'off',
      'max-statements': 'off',
    },
  },
  {
    files: ['**/*.html'],
    extends: [angular.configs.templateRecommended, angular.configs.templateAccessibility],
    rules: {},
  },
]);
