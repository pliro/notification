(function() {
    'use strict';

    angular
        .module('notificationApp')
        .controller('NotificationEventReminderDetailController', NotificationEventReminderDetailController);

    NotificationEventReminderDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'NotificationEventReminder', 'NotificationChannel', 'NotificationEvent', 'NotificationNotifieeType', 'Organization'];

    function NotificationEventReminderDetailController($scope, $rootScope, $stateParams, entity, NotificationEventReminder, NotificationChannel, NotificationEvent, NotificationNotifieeType, Organization) {
        var vm = this;
        vm.notificationEventReminder = entity;
        
        var unsubscribe = $rootScope.$on('notificationApp:notificationEventReminderUpdate', function(event, result) {
            vm.notificationEventReminder = result;
        });
        $scope.$on('$destroy', unsubscribe);

    }
})();
