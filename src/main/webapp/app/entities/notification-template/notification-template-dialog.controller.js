(function() {
    'use strict';

    angular
        .module('notificationApp')
        .controller('NotificationTemplateDialogController', NotificationTemplateDialogController);

    NotificationTemplateDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'NotificationTemplate', 'NotificationChannel', 'NotificationEvent', 'NotificationNotifieeType', 'Organization'];

    function NotificationTemplateDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, NotificationTemplate, NotificationChannel, NotificationEvent, NotificationNotifieeType, Organization) {
        var vm = this;
        vm.notificationTemplate = entity;
        vm.notificationchannels = NotificationChannel.query();
        vm.notificationevents = NotificationEvent.query();
        vm.notificationnotifieetypes = NotificationNotifieeType.query();
        vm.organizations = Organization.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        var onSaveSuccess = function (result) {
            $scope.$emit('notificationApp:notificationTemplateUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        };

        var onSaveError = function () {
            vm.isSaving = false;
        };

        vm.save = function () {
            vm.isSaving = true;
            if (vm.notificationTemplate.id !== null) {
                NotificationTemplate.update(vm.notificationTemplate, onSaveSuccess, onSaveError);
            } else {
                NotificationTemplate.save(vm.notificationTemplate, onSaveSuccess, onSaveError);
            }
        };

        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
    }
})();
