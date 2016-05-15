(function() {
    'use strict';

    angular
        .module('notificationApp')
        .controller('NotificationEventController', NotificationEventController);

    NotificationEventController.$inject = ['$scope', '$state', 'NotificationEvent', 'NotificationEventSearch'];

    function NotificationEventController ($scope, $state, NotificationEvent, NotificationEventSearch) {
        var vm = this;
        vm.notificationEvents = [];
        vm.loadAll = function() {
            NotificationEvent.query(function(result) {
                vm.notificationEvents = result;
            });
        };

        vm.search = function () {
            if (!vm.searchQuery) {
                return vm.loadAll();
            }
            NotificationEventSearch.query({query: vm.searchQuery}, function(result) {
                vm.notificationEvents = result;
            });
        };
        vm.loadAll();
        
    }
})();
