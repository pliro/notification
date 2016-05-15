(function() {
    'use strict';

    angular
        .module('notificationApp')
        .controller('OrganizationController', OrganizationController);

    OrganizationController.$inject = ['$scope', '$state', 'Organization', 'OrganizationSearch'];

    function OrganizationController ($scope, $state, Organization, OrganizationSearch) {
        var vm = this;
        vm.organizations = [];
        vm.loadAll = function() {
            Organization.query(function(result) {
                vm.organizations = result;
            });
        };

        vm.search = function () {
            if (!vm.searchQuery) {
                return vm.loadAll();
            }
            OrganizationSearch.query({query: vm.searchQuery}, function(result) {
                vm.organizations = result;
            });
        };
        vm.loadAll();
        
    }
})();
