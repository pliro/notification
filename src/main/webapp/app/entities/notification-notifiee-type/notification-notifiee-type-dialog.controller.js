(function() {
    'use strict';

    angular
        .module('notificationApp')
        .controller('NotificationNotifieeTypeDialogController', NotificationNotifieeTypeDialogController);

    NotificationNotifieeTypeDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'NotificationNotifieeType', 'NotificationTemplate', 'NotificationEventReminder'];

    function NotificationNotifieeTypeDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, NotificationNotifieeType, NotificationTemplate, NotificationEventReminder) {
        var vm = this;
        vm.notificationNotifieeType = entity;
        vm.notificationtemplates = NotificationTemplate.query();
        vm.notificationeventreminders = NotificationEventReminder.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        var onSaveSuccess = function (result) {
            $scope.$emit('notificationApp:notificationNotifieeTypeUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        };

        var onSaveError = function () {
            vm.isSaving = false;
        };

        vm.save = function () {
            vm.isSaving = true;
            if (vm.notificationNotifieeType.id !== null) {
                NotificationNotifieeType.update(vm.notificationNotifieeType, onSaveSuccess, onSaveError);
            } else {
                NotificationNotifieeType.save(vm.notificationNotifieeType, onSaveSuccess, onSaveError);
            }
        };

        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
    }
})();
