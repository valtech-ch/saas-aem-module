const { merge } = require('webpack-merge')
const common = require('./webpack.common.js')
const path = require('path')
const HtmlWebpackPlugin = require('html-webpack-plugin')

const SOURCE_ROOT = __dirname + '/src/main/webpack'

module.exports = (env) => {
  return merge(common, {
    mode: 'development',
    devtool: 'inline-source-map',
    performance: { hints: false },
    plugins: [
      new HtmlWebpackPlugin({
        template: path.resolve(__dirname, SOURCE_ROOT + '/static/index.html'),
      }),
    ],
    devServer: {
      proxy: [
        {
          context: ['/content', '/etc.clientlibs'],
          target: 'http://localhost:4502',
        },
      ],
      liveReload: false,
      devMiddleware: {
        writeToDisk: true,
      },
    },
  })
}
