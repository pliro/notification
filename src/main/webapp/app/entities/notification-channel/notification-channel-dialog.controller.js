(function() {
    'use strict';

    angular
        .module('notificationApp')
        .controller('NotificationChannelDialogController', NotificationChannelDialogController);

    NotificationChannelDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'NotificationChannel', 'NotificationTemplate', 'NotificationEventReminder'];

    function NotificationChannelDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, NotificationChannel, NotificationTemplate, NotificationEventReminder) {
        var vm = this;
        vm.notificationChannel = entity;
        vm.notificationtemplates = NotificationTemplate.query();
        vm.notificationeventreminders = NotificationEventReminder.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        var onSaveSuccess = function (result) {
            $scope.$emit('notificationApp:notificationChannelUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        };

        var onSaveError = function () {
            vm.isSaving = false;
        };

        vm.save = function () {
            vm.isSaving = true;
            if (vm.notificationChannel.id !== null) {
                NotificationChannel.update(vm.notificationChannel, onSaveSuccess, onSaveError);
            } else {
                NotificationChannel.save(vm.notificationChannel, onSaveSuccess, onSaveError);
            }
        };

        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
    }
})();
