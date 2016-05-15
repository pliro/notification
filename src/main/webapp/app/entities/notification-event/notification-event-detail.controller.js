(function() {
    'use strict';

    angular
        .module('notificationApp')
        .controller('NotificationEventDetailController', NotificationEventDetailController);

    NotificationEventDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'NotificationEvent', 'NotificationTemplate', 'NotificationEventReminder'];

    function NotificationEventDetailController($scope, $rootScope, $stateParams, entity, NotificationEvent, NotificationTemplate, NotificationEventReminder) {
        var vm = this;
        vm.notificationEvent = entity;
        
        var unsubscribe = $rootScope.$on('notificationApp:notificationEventUpdate', function(event, result) {
            vm.notificationEvent = result;
        });
        $scope.$on('$destroy', unsubscribe);

    }
})();
