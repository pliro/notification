(function() {
    'use strict';

    angular
        .module('notificationApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('notification-notifiee-type', {
            parent: 'entity',
            url: '/notification-notifiee-type',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'NotificationNotifieeTypes'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/notification-notifiee-type/notification-notifiee-types.html',
                    controller: 'NotificationNotifieeTypeController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
            }
        })
        .state('notification-notifiee-type-detail', {
            parent: 'entity',
            url: '/notification-notifiee-type/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'NotificationNotifieeType'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/notification-notifiee-type/notification-notifiee-type-detail.html',
                    controller: 'NotificationNotifieeTypeDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'NotificationNotifieeType', function($stateParams, NotificationNotifieeType) {
                    return NotificationNotifieeType.get({id : $stateParams.id});
                }]
            }
        })
        .state('notification-notifiee-type.new', {
            parent: 'notification-notifiee-type',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/notification-notifiee-type/notification-notifiee-type-dialog.html',
                    controller: 'NotificationNotifieeTypeDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                notificationNotifieeTypeId: null,
                                notificationNotifieeTypeName: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('notification-notifiee-type', null, { reload: true });
                }, function() {
                    $state.go('notification-notifiee-type');
                });
            }]
        })
        .state('notification-notifiee-type.edit', {
            parent: 'notification-notifiee-type',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/notification-notifiee-type/notification-notifiee-type-dialog.html',
                    controller: 'NotificationNotifieeTypeDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['NotificationNotifieeType', function(NotificationNotifieeType) {
                            return NotificationNotifieeType.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('notification-notifiee-type', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('notification-notifiee-type.delete', {
            parent: 'notification-notifiee-type',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/notification-notifiee-type/notification-notifiee-type-delete-dialog.html',
                    controller: 'NotificationNotifieeTypeDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['NotificationNotifieeType', function(NotificationNotifieeType) {
                            return NotificationNotifieeType.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('notification-notifiee-type', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
