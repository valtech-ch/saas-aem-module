/*~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 ~ Copyright 2020 Adobe Systems Incorporated
 ~
 ~ Licensed under the Apache License, Version 2.0 (the "License");
 ~ you may not use this file except in compliance with the License.
 ~ You may obtain a copy of the License at
 ~
 ~     http://www.apache.org/licenses/LICENSE-2.0
 ~
 ~ Unless required by applicable law or agreed to in writing, software
 ~ distributed under the License is distributed on an "AS IS" BASIS,
 ~ WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 ~ See the License for the specific language governing permissions and
 ~ limitations under the License.
 ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/

const path = require('path')

const BUILD_DIR = path.join(__dirname, 'dist')
const CLIENTLIB_DIR = path.join(
  __dirname,
  '..',
  'ui.apps',
  'src',
  'main',
  'content',
  'jcr_root',
  'apps',
  'saas-aem-module',
  'clientlibs',
)

const libsBaseConfig = {
  allowProxy: true,
  serializationFormat: 'xml',
  cssProcessor: ['default:none', 'min:none'],
  jsProcessor: ['default:none', 'min:none'],
}

// Config for `aem-clientlib-generator`
module.exports = {
  context: BUILD_DIR,
  clientLibRoot: CLIENTLIB_DIR,
  libs: [
    {
      ...libsBaseConfig,
      name: 'clientlib-dependencies',
      categories: ['saas-aem-module.dependencies'],
      assets: {
        // Copy entrypoint scripts and stylesheets into the respective ClientLib
        // directories
        js: {
          cwd: 'clientlib-dependencies',
          files: ['**/*.js'],
          flatten: false,
        },
        css: {
          cwd: 'clientlib-dependencies',
          files: ['**/*.css'],
          flatten: false,
        },
      },
    },
    {
      ...libsBaseConfig,
      name: 'clientlib-base',
      categories: ['saas-aem-module.base'],
      dependencies: ['saas-aem-module.dependencies'],
      assets: {
        // Copy entrypoint scripts into the respective ClientLib
        // directories
        js: {
          cwd: 'clientlib-base',
          files: ['**/*.js'],
          flatten: false,
        },
      },
    },
    {
      ...libsBaseConfig,
      name: 'wknd-sample',
      categories: ['saas-aem-module.wknd.sample'],
      dependencies: ['saas-aem-module.dependencies'],
      assets: {
        // Copy entrypoint stylesheets into the respective ClientLib
        // directories
        css: {
          cwd: 'clientlib-base',
          files: ['**/*.css'],
          flatten: false,
        },

        // Copy all other files into the `resources` ClientLib directory
        resources: {
          cwd: 'clientlib-base',
          files: ['**/*.*'],
          flatten: false,
          ignore: ['**/*.js', '**/*.css'],
        },
      },
    },
  ],
}
