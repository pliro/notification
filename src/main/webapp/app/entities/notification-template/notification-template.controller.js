(function() {
    'use strict';

    angular
        .module('notificationApp')
        .controller('NotificationTemplateController', NotificationTemplateController);

    NotificationTemplateController.$inject = ['$scope', '$state', 'NotificationTemplate', 'NotificationTemplateSearch'];

    function NotificationTemplateController ($scope, $state, NotificationTemplate, NotificationTemplateSearch) {
        var vm = this;
        vm.notificationTemplates = [];
        vm.loadAll = function() {
            NotificationTemplate.query(function(result) {
                vm.notificationTemplates = result;
            });
        };

        vm.search = function () {
            if (!vm.searchQuery) {
                return vm.loadAll();
            }
            NotificationTemplateSearch.query({query: vm.searchQuery}, function(result) {
                vm.notificationTemplates = result;
            });
        };
        vm.loadAll();
        
    }
})();
