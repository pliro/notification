(function() {
    'use strict';

    angular
        .module('notificationApp')
        .controller('NotificationTemplateDeleteController',NotificationTemplateDeleteController);

    NotificationTemplateDeleteController.$inject = ['$uibModalInstance', 'entity', 'NotificationTemplate'];

    function NotificationTemplateDeleteController($uibModalInstance, entity, NotificationTemplate) {
        var vm = this;
        vm.notificationTemplate = entity;
        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        vm.confirmDelete = function (id) {
            NotificationTemplate.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };
    }
})();
