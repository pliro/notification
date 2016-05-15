(function() {
    'use strict';

    angular
        .module('notificationApp')
        .controller('NotificationChannelController', NotificationChannelController);

    NotificationChannelController.$inject = ['$scope', '$state', 'NotificationChannel', 'NotificationChannelSearch'];

    function NotificationChannelController ($scope, $state, NotificationChannel, NotificationChannelSearch) {
        var vm = this;
        vm.notificationChannels = [];
        vm.loadAll = function() {
            NotificationChannel.query(function(result) {
                vm.notificationChannels = result;
            });
        };

        vm.search = function () {
            if (!vm.searchQuery) {
                return vm.loadAll();
            }
            NotificationChannelSearch.query({query: vm.searchQuery}, function(result) {
                vm.notificationChannels = result;
            });
        };
        vm.loadAll();
        
    }
})();
