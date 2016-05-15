(function() {
    'use strict';

    angular
        .module('notificationApp')
        .controller('NotificationEventReminderDeleteController',NotificationEventReminderDeleteController);

    NotificationEventReminderDeleteController.$inject = ['$uibModalInstance', 'entity', 'NotificationEventReminder'];

    function NotificationEventReminderDeleteController($uibModalInstance, entity, NotificationEventReminder) {
        var vm = this;
        vm.notificationEventReminder = entity;
        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        vm.confirmDelete = function (id) {
            NotificationEventReminder.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };
    }
})();
