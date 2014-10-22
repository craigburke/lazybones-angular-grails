'use strict';

function FlashService($rootScope) {
    var FlashService = {};

    var MESSAGE_TYPE = {
        ERROR: 'error',
        SUCCESS: 'success',
        INFO: 'info',
        WARN: 'warn'
    };

	var _message, _clearRequestCount, _routeChangePersist;

    var broadcastChange = function() {
        $rootScope.$broadcast('flash:messageChange');
    };
	
	var resetMessage = function() {
        _message = null;
		_clearRequestCount = 0;
	}
	
    var clearMessage = function() {
		_clearRequestCount++;
		
		if (!_routeChangePersist || _clearRequestCount >= 2) {
			resetMessage();
	        broadcastChange();	
		}
    }

    FlashService.TYPE = MESSAGE_TYPE;

    FlashService.setMessage = function(message, options) {
		options = options || {};
        var type = options.type || MESSAGE_TYPE.INFO;
    
        _message = {message: message, type: type};
		_routeChangePersist = options.routeChange || false;
		_clearRequestCount = 0;
		
        broadcastChange();
	};
	
	FlashService.error = function(message, options) {
		options = options || {};
		options.type = MESSAGE_TYPE.ERROR;
		FlashService.setMessage(message, options);
	};
	
	FlashService.success = function(message, options) {
		options = options || {};
		options.type = MESSAGE_TYPE.SUCCESS;
		FlashService.setMessage(message, options);
	};
	
	FlashService.warn = function(message, options) {
		options = options || {};
		options.type = MESSAGE_TYPE.WARN;
		FlashService.setMessage(message, options);
	};
	
	FlashService.info = function(message, options) {
		options = options || {};
		options.type = MESSAGE_TYPE.INFO;
		FlashService.setMessage(message, options);
	};

    FlashService.getMessage = function() {
		if (_routeChangePersist && _clearRequestCount == 0) {
			return null;
		}
		else {
	        return _message;
		}
    };

    FlashService.clear = function() {
        clearMessage();
    };

	resetMessage();
    return FlashService;
}

angular.module('grails.services.flash', [])
    .factory('FlashService', FlashService);