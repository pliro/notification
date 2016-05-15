(function() {
    'use strict';

    angular
        .module('notificationApp')
        .controller('NotificationEventReminderDialogController', NotificationEventReminderDialogController);

    NotificationEventReminderDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'NotificationEventReminder', 'NotificationChannel', 'NotificationEvent', 'NotificationNotifieeType', 'Organization'];

    function NotificationEventReminderDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, NotificationEventReminder, NotificationChannel, NotificationEvent, NotificationNotifieeType, Organization) {
        var vm = this;
        vm.notificationEventReminder = entity;
        vm.notificationchannels = NotificationChannel.query();
        vm.notificationevents = NotificationEvent.query();
        vm.notificationnotifieetypes = NotificationNotifieeType.query();
        vm.organizations = Organization.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        var onSaveSuccess = function (result) {
            $scope.$emit('notificationApp:notificationEventReminderUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        };

        var onSaveError = function () {
            vm.isSaving = false;
        };

        vm.save = function () {
            vm.isSaving = true;
            if (vm.notificationEventReminder.id !== null) {
                NotificationEventReminder.update(vm.notificationEventReminder, onSaveSuccess, onSaveError);
            } else {
                NotificationEventReminder.save(vm.notificationEventReminder, onSaveSuccess, onSaveError);
            }
        };

        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
    }
})();
