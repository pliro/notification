(function() {
    'use strict';
    angular
        .module('notificationApp')
        .factory('NotificationChannel', NotificationChannel);

    NotificationChannel.$inject = ['$resource'];

    function NotificationChannel ($resource) {
        var resourceUrl =  'api/notification-channels/:id';

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
