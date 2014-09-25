'use strict';

function FlashService($rootScope) {
    var FlashService = {};

    var MESSAGE_TYPE = {
        ERROR: 'error',
        SUCCESS: 'success',
        INFO: 'info',
        WARN: 'warn'
    };

    var _message = null;

    var broadcastChange = function() {
        $rootScope.$broadcast('flash:messageChange');
    };

    var clearMessage = function() {
        _message = null;
        broadcastChange();
    }

    var setMessage = function(message, type, title) {
        _message = {message: message, type: type, title: title};
        broadcastChange();
    };

    FlashService.TYPES = MESSAGE_TYPE;

    FlashService.error = function(message, title) {
        setMessage(message, MESSAGE_TYPE.ERROR, title);
    };

    FlashService.success = function(message, title) {
        setMessage(message, MESSAGE_TYPE.SUCCESS, title);
    };

    FlashService.info = function(message, title) {
        setMessage(message, MESSAGE_TYPE.INFO, title);
    };

    FlashService.warn = function(message, title) {
        setMessage(message, MESSAGE_TYPE.WARN, title);
    };

    FlashService.set = function(message, type, title) {
        type = type ? type : MESSAGE_TYPE.INFO;
        setMessage(message, type, title);
    };

    FlashService.get = function() {
        return _message;
    };

    FlashService.clear = function() {
        clearMessage();
    };

    return FlashService;
}

angular.module('grails.services.flash', [])
    .factory('FlashService', FlashService);