(function() {
    'use strict';

    angular
        .module('notificationApp')
        .controller('NotificationChannelDetailController', NotificationChannelDetailController);

    NotificationChannelDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'NotificationChannel', 'NotificationTemplate', 'NotificationEventReminder'];

    function NotificationChannelDetailController($scope, $rootScope, $stateParams, entity, NotificationChannel, NotificationTemplate, NotificationEventReminder) {
        var vm = this;
        vm.notificationChannel = entity;
        
        var unsubscribe = $rootScope.$on('notificationApp:notificationChannelUpdate', function(event, result) {
            vm.notificationChannel = result;
        });
        $scope.$on('$destroy', unsubscribe);

    }
})();