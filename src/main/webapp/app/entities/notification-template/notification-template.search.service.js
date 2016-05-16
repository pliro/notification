(function() {
    'use strict';

    angular
        .module('notificationApp')
        .factory('NotificationTemplateSearch', NotificationTemplateSearch);

    NotificationTemplateSearch.$inject = ['$resource'];

    function NotificationTemplateSearch($resource) {
        var resourceUrl =  'api/_search/notification-templates/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
