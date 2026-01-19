/** @type {import("prettier").Config} */
const config = {
  printWidth: 100,
  singleQuote: true,
  arrowParens: 'avoid',
  semi: true,
  trailingComma: 'es5',
  overrides: [
    {
      files: '*.html',
      options: {
        parser: 'angular',
      },
    },
  ],
};

export default config;
