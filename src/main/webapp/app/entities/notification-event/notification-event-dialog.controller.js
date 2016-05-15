(function() {
    'use strict';

    angular
        .module('notificationApp')
        .controller('NotificationEventDialogController', NotificationEventDialogController);

    NotificationEventDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'NotificationEvent', 'NotificationTemplate', 'NotificationEventReminder'];

    function NotificationEventDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, NotificationEvent, NotificationTemplate, NotificationEventReminder) {
        var vm = this;
        vm.notificationEvent = entity;
        vm.notificationtemplates = NotificationTemplate.query();
        vm.notificationeventreminders = NotificationEventReminder.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        var onSaveSuccess = function (result) {
            $scope.$emit('notificationApp:notificationEventUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        };

        var onSaveError = function () {
            vm.isSaving = false;
        };

        vm.save = function () {
            vm.isSaving = true;
            if (vm.notificationEvent.id !== null) {
                NotificationEvent.update(vm.notificationEvent, onSaveSuccess, onSaveError);
            } else {
                NotificationEvent.save(vm.notificationEvent, onSaveSuccess, onSaveError);
            }
        };

        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
    }
})();
