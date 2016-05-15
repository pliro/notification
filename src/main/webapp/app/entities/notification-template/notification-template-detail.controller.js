(function() {
    'use strict';

    angular
        .module('notificationApp')
        .controller('NotificationTemplateDetailController', NotificationTemplateDetailController);

    NotificationTemplateDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'NotificationTemplate', 'NotificationChannel', 'NotificationEvent', 'NotificationNotifieeType', 'Organization'];

    function NotificationTemplateDetailController($scope, $rootScope, $stateParams, entity, NotificationTemplate, NotificationChannel, NotificationEvent, NotificationNotifieeType, Organization) {
        var vm = this;
        vm.notificationTemplate = entity;
        
        var unsubscribe = $rootScope.$on('notificationApp:notificationTemplateUpdate', function(event, result) {
            vm.notificationTemplate = result;
        });
        $scope.$on('$destroy', unsubscribe);

    }
})();
