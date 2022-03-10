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
      configFile: './tsconfig.json',
    }),
  ],
}

module.exports = merge(
  {},
  {
    mode: 'production',
    resolve: resolve,
    entry: {
      base: SOURCE_ROOT + '/site/js/initSearch.ts',
    },
    devtool: 'source-map',
    output: {
      libraryTarget: 'umd',
      filename: 'saas.js',
      library: 'saas',
      umdNamedDefine: true,
      globalObject: 'this',
      path: path.resolve(__dirname, './package'),
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
