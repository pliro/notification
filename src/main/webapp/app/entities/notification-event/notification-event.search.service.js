(function() {
    'use strict';

    angular
        .module('notificationApp')
        .factory('NotificationEventSearch', NotificationEventSearch);

    NotificationEventSearch.$inject = ['$resource'];

    function NotificationEventSearch($resource) {
        var resourceUrl =  'api/_search/notification-events/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
