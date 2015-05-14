module.exports = function(config) {

  var ASSET_PATH = 'grails-app/assets';
  var TEST_PATH = 'src/test/js'

  config.set({
    basePath: '../../../',
    frameworks: ['jasmine'],

    files: [
      ASSET_PATH + '/vendor/angular/angular.js',
      ASSET_PATH + '/vendor/**/*.js',
      ASSET_PATH + '/javascripts/**/*.js',
      TEST_PATH + '/angular-mocks.js',
      TEST_PATH + '/**/*-spec.js'
    ],

    exclude: [],
    preprocessors: {},
    reporters: ['progress'],
    port: 9876,
    colors: true,
    logLevel: config.LOG_ERROR,
    autoWatch: true,
    browsers: ['PhantomJS'],
    singleRun: false
  });
};
