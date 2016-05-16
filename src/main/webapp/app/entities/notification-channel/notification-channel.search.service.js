(function() {
    'use strict';

    angular
        .module('notificationApp')
        .factory('NotificationChannelSearch', NotificationChannelSearch);

    NotificationChannelSearch.$inject = ['$resource'];

    function NotificationChannelSearch($resource) {
        var resourceUrl =  'api/_search/notification-channels/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
