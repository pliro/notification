(function() {
    'use strict';
    angular
        .module('notificationApp')
        .factory('NotificationEventReminder', NotificationEventReminder);

    NotificationEventReminder.$inject = ['$resource'];

    function NotificationEventReminder ($resource) {
        var resourceUrl =  'api/notification-event-reminders/:id';

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
