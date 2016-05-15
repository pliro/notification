(function() {
    'use strict';

    angular
        .module('notificationApp')
        .factory('NotificationNotifieeTypeSearch', NotificationNotifieeTypeSearch);

    NotificationNotifieeTypeSearch.$inject = ['$resource'];

    function NotificationNotifieeTypeSearch($resource) {
        var resourceUrl =  'api/_search/notification-notifiee-types/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
