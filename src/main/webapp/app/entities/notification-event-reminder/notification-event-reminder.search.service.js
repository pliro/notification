(function() {
    'use strict';

    angular
        .module('notificationApp')
        .factory('NotificationEventReminderSearch', NotificationEventReminderSearch);

    NotificationEventReminderSearch.$inject = ['$resource'];

    function NotificationEventReminderSearch($resource) {
        var resourceUrl =  'api/_search/notification-event-reminders/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
