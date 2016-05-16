(function() {
    'use strict';

    angular
        .module('notificationApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('notification-channel', {
            parent: 'entity',
            url: '/notification-channel',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'NotificationChannels'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/notification-channel/notification-channels.html',
                    controller: 'NotificationChannelController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
            }
        })
        .state('notification-channel-detail', {
            parent: 'entity',
            url: '/notification-channel/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'NotificationChannel'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/notification-channel/notification-channel-detail.html',
                    controller: 'NotificationChannelDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'NotificationChannel', function($stateParams, NotificationChannel) {
                    return NotificationChannel.get({id : $stateParams.id});
                }]
            }
        })
        .state('notification-channel.new', {
            parent: 'notification-channel',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/notification-channel/notification-channel-dialog.html',
                    controller: 'NotificationChannelDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                notificationChannelId: null,
                                notificationChannelName: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('notification-channel', null, { reload: true });
                }, function() {
                    $state.go('notification-channel');
                });
            }]
        })
        .state('notification-channel.edit', {
            parent: 'notification-channel',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/notification-channel/notification-channel-dialog.html',
                    controller: 'NotificationChannelDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['NotificationChannel', function(NotificationChannel) {
                            return NotificationChannel.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('notification-channel', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('notification-channel.delete', {
            parent: 'notification-channel',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/notification-channel/notification-channel-delete-dialog.html',
                    controller: 'NotificationChannelDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['NotificationChannel', function(NotificationChannel) {
                            return NotificationChannel.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('notification-channel', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
