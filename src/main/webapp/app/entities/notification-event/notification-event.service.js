(function() {
    'use strict';
    angular
        .module('notificationApp')
        .factory('NotificationEvent', NotificationEvent);

    NotificationEvent.$inject = ['$resource'];

    function NotificationEvent ($resource) {
        var resourceUrl =  'api/notification-events/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
