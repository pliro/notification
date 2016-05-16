(function() {
    'use strict';

    angular
        .module('notificationApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('notification-event', {
            parent: 'entity',
            url: '/notification-event',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'NotificationEvents'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/notification-event/notification-events.html',
                    controller: 'NotificationEventController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
            }
        })
        .state('notification-event-detail', {
            parent: 'entity',
            url: '/notification-event/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'NotificationEvent'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/notification-event/notification-event-detail.html',
                    controller: 'NotificationEventDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'NotificationEvent', function($stateParams, NotificationEvent) {
                    return NotificationEvent.get({id : $stateParams.id});
                }]
            }
        })
        .state('notification-event.new', {
            parent: 'notification-event',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/notification-event/notification-event-dialog.html',
                    controller: 'NotificationEventDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                notificationEventId: null,
                                notificationEventName: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('notification-event', null, { reload: true });
                }, function() {
                    $state.go('notification-event');
                });
            }]
        })
        .state('notification-event.edit', {
            parent: 'notification-event',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/notification-event/notification-event-dialog.html',
                    controller: 'NotificationEventDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['NotificationEvent', function(NotificationEvent) {
                            return NotificationEvent.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('notification-event', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('notification-event.delete', {
            parent: 'notification-event',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/notification-event/notification-event-delete-dialog.html',
                    controller: 'NotificationEventDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['NotificationEvent', function(NotificationEvent) {
                            return NotificationEvent.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('notification-event', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
