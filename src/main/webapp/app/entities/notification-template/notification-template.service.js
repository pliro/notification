(function() {
    'use strict';
    angular
        .module('notificationApp')
        .factory('NotificationTemplate', NotificationTemplate);

    NotificationTemplate.$inject = ['$resource'];

    function NotificationTemplate ($resource) {
        var resourceUrl =  'api/notification-templates/:id';

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
