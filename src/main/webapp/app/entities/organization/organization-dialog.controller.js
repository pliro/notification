(function() {
    'use strict';

    angular
        .module('notificationApp')
        .controller('OrganizationDialogController', OrganizationDialogController);

    OrganizationDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Organization', 'NotificationTemplate'];

    function OrganizationDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Organization, NotificationTemplate) {
        var vm = this;
        vm.organization = entity;
        vm.notificationtemplates = NotificationTemplate.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        var onSaveSuccess = function (result) {
            $scope.$emit('notificationApp:organizationUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        };

        var onSaveError = function () {
            vm.isSaving = false;
        };

        vm.save = function () {
            vm.isSaving = true;
            if (vm.organization.id !== null) {
                Organization.update(vm.organization, onSaveSuccess, onSaveError);
            } else {
                Organization.save(vm.organization, onSaveSuccess, onSaveError);
            }
        };

        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
    }
})();
