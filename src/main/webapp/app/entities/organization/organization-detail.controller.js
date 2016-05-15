(function() {
    'use strict';

    angular
        .module('notificationApp')
        .controller('OrganizationDetailController', OrganizationDetailController);

    OrganizationDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'Organization', 'NotificationTemplate', 'NotificationEventReminder'];

    function OrganizationDetailController($scope, $rootScope, $stateParams, entity, Organization, NotificationTemplate, NotificationEventReminder) {
        var vm = this;
        vm.organization = entity;
        
        var unsubscribe = $rootScope.$on('notificationApp:organizationUpdate', function(event, result) {
            vm.organization = result;
        });
        $scope.$on('$destroy', unsubscribe);

    }
})();
