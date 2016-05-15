(function() {
    'use strict';

    angular
        .module('notificationApp')
        .controller('NotificationChannelDeleteController',NotificationChannelDeleteController);

    NotificationChannelDeleteController.$inject = ['$uibModalInstance', 'entity', 'NotificationChannel'];

    function NotificationChannelDeleteController($uibModalInstance, entity, NotificationChannel) {
        var vm = this;
        vm.notificationChannel = entity;
        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        vm.confirmDelete = function (id) {
            NotificationChannel.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };
    }
})();
