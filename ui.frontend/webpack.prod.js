/* eslint-disable @typescript-eslint/no-var-requires */
const { merge } = require('webpack-merge')
const CSSMinimizerWebpackPlugin = require('css-minimizer-webpack-plugin')
const TerserPlugin = require('terser-webpack-plugin')
const common = require('./webpack.common.js')

module.exports = merge(common, {
  mode: 'production',
  optimization: {
    minimize: true,
    minimizer: [
      new TerserPlugin(),
      new CSSMinimizerWebpackPlugin({
        parallel: true,
      }),
    ],
    splitChunks: {
      cacheGroups: {
        main: {
          chunks: 'all',
          name: 'site',
          test: 'main',
          enforce: true,
        },
      },
    },
  },
  performance: { hints: false },
})
