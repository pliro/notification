(function() {
    'use strict';

    angular
        .module('notificationApp')
        .controller('NotificationEventDeleteController',NotificationEventDeleteController);

    NotificationEventDeleteController.$inject = ['$uibModalInstance', 'entity', 'NotificationEvent'];

    function NotificationEventDeleteController($uibModalInstance, entity, NotificationEvent) {
        var vm = this;
        vm.notificationEvent = entity;
        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        vm.confirmDelete = function (id) {
            NotificationEvent.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };
    }
})();
