(function() {
    'use strict';
    angular
        .module('notificationApp')
        .factory('NotificationNotifieeType', NotificationNotifieeType);

    NotificationNotifieeType.$inject = ['$resource'];

    function NotificationNotifieeType ($resource) {
        var resourceUrl =  'api/notification-notifiee-types/:id';

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
