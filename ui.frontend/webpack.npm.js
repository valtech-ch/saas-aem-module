const { merge } = require('webpack-merge')
const webpack = require('webpack')
const common = require('./webpack.common.js')
const path = require('path')
const TSConfigPathsPlugin = require('tsconfig-paths-webpack-plugin')
const MiniCssExtractPlugin = require('mini-css-extract-plugin')
const { CleanWebpackPlugin } = require('clean-webpack-plugin')
const CopyWebpackPlugin = require('copy-webpack-plugin')

const SOURCE_ROOT = __dirname + '/src/main/webpack'

const resolve = {
  extensions: ['.js', '.ts'],
  plugins: [
    new TSConfigPathsPlugin({
      configFile: './tsconfig.npm.json',
    }),
  ],
}

module.exports = merge(
  {},
  {
    mode: 'production',
    resolve: resolve,
    entry: {
      site: SOURCE_ROOT + '/site/js/initSearch.ts',
    },
    output: {
      path: path.resolve(__dirname, './package'),
      filename: 'saas.js',
      libraryTarget: 'umd',
      globalObject: 'this',
    },
    optimization: {
      minimize: false,
    },
    module: {
      rules: [
        {
          test: /\.[tj]sx?$/,
          exclude: /node_modules/,
          use: [
            {
              loader: 'babel-loader',
              options: {
                configFile: './babel.config.npm.js',
              },
            },
          ],
        },
      ],
    },
    plugins: [new CleanWebpackPlugin()],
  },
)
