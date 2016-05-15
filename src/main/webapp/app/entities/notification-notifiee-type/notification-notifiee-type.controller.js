(function() {
    'use strict';

    angular
        .module('notificationApp')
        .controller('NotificationNotifieeTypeController', NotificationNotifieeTypeController);

    NotificationNotifieeTypeController.$inject = ['$scope', '$state', 'NotificationNotifieeType', 'NotificationNotifieeTypeSearch'];

    function NotificationNotifieeTypeController ($scope, $state, NotificationNotifieeType, NotificationNotifieeTypeSearch) {
        var vm = this;
        vm.notificationNotifieeTypes = [];
        vm.loadAll = function() {
            NotificationNotifieeType.query(function(result) {
                vm.notificationNotifieeTypes = result;
            });
        };

        vm.search = function () {
            if (!vm.searchQuery) {
                return vm.loadAll();
            }
            NotificationNotifieeTypeSearch.query({query: vm.searchQuery}, function(result) {
                vm.notificationNotifieeTypes = result;
            });
        };
        vm.loadAll();
        
    }
})();
