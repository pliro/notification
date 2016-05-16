(function() {
    'use strict';

    angular
        .module('notificationApp')
        .controller('NotificationNotifieeTypeDetailController', NotificationNotifieeTypeDetailController);

    NotificationNotifieeTypeDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'NotificationNotifieeType', 'NotificationTemplate'];

    function NotificationNotifieeTypeDetailController($scope, $rootScope, $stateParams, entity, NotificationNotifieeType, NotificationTemplate) {
        var vm = this;
        vm.notificationNotifieeType = entity;
        
        var unsubscribe = $rootScope.$on('notificationApp:notificationNotifieeTypeUpdate', function(event, result) {
            vm.notificationNotifieeType = result;
        });
        $scope.$on('$destroy', unsubscribe);

    }
})();
