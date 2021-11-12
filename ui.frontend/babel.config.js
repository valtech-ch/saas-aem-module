module.exports = {
  presets: ['@babel/preset-env', '@babel/typescript'],
  plugins: [
    '@babel/plugin-proposal-class-properties',
    [
      '@babel/transform-runtime',
      {
        regenerator: true,
      },
    ],
  ],
}
