(function() {
    'use strict';

    angular
        .module('notificationApp')
        .controller('NotificationNotifieeTypeDeleteController',NotificationNotifieeTypeDeleteController);

    NotificationNotifieeTypeDeleteController.$inject = ['$uibModalInstance', 'entity', 'NotificationNotifieeType'];

    function NotificationNotifieeTypeDeleteController($uibModalInstance, entity, NotificationNotifieeType) {
        var vm = this;
        vm.notificationNotifieeType = entity;
        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        vm.confirmDelete = function (id) {
            NotificationNotifieeType.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };
    }
})();
