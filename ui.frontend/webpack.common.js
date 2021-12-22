'use strict'

const path = require('path')
const webpack = require('webpack')
const MiniCssExtractPlugin = require('mini-css-extract-plugin')
const TSConfigPathsPlugin = require('tsconfig-paths-webpack-plugin')
const CopyWebpackPlugin = require('copy-webpack-plugin')
const { CleanWebpackPlugin } = require('clean-webpack-plugin')

const SOURCE_ROOT = __dirname + '/src/main/webpack'

const resolve = {
  extensions: ['.js', '.ts'],
  plugins: [
    new TSConfigPathsPlugin({
      configFile: './tsconfig.json',
    }),
  ],
}

module.exports = {
  resolve: resolve,
  entry: {
    base: SOURCE_ROOT + '/site/main.ts',
  },
  output: {
    filename: (chunkData) => {
      return chunkData.chunk.name === 'dependencies'
          ? 'clientlib-dependencies/[name].js'
          : 'clientlib-base/[name].js'
    },
    path: path.resolve(__dirname, 'dist'),
  },
  module: {
    rules: [
      {
        test: /\.[tj]sx?$/,
        exclude: /node_modules|package/,
        use: [
          {
            loader: 'babel-loader',
          },
          {
            loader: 'glob-import-loader',
            options: {
              resolve: resolve,
            },
          },
        ],
      },
      {
        test: /\.scss$/,
        use: [
          MiniCssExtractPlugin.loader,
          {
            loader: 'css-loader',
          },
          {
            loader: 'postcss-loader',
            options: {
              postcssOptions: {
                plugins: ['autoprefixer'],
              },
            },
          },
          {
            loader: 'sass-loader',
          },
          {
            loader: 'glob-import-loader',
            options: {
              resolve: resolve,
            },
          },
        ],
      },
    ],
  },
  plugins: [
    new CleanWebpackPlugin(),
    new webpack.NoEmitOnErrorsPlugin(),
    new MiniCssExtractPlugin({
      filename: 'clientlib-[name]/[name].css',
    }),
    new CopyWebpackPlugin({
      patterns: [
        {
          from: path.resolve(__dirname, SOURCE_ROOT + '/resources'),
          to: './clientlib-base/',
        },
      ],
    }),
  ],
  stats: {
    assetsSort: 'chunks',
    builtAt: true,
    children: false,
    chunkGroups: true,
    chunkOrigins: true,
    colors: false,
    errors: true,
    errorDetails: true,
    env: true,
    modules: false,
    performance: true,
    providedExports: false,
    source: false,
    warnings: true,
  },
}
