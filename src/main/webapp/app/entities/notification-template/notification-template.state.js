(function() {
    'use strict';

    angular
        .module('notificationApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('notification-template', {
            parent: 'entity',
            url: '/notification-template',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'NotificationTemplates'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/notification-template/notification-templates.html',
                    controller: 'NotificationTemplateController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
            }
        })
        .state('notification-template-detail', {
            parent: 'entity',
            url: '/notification-template/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'NotificationTemplate'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/notification-template/notification-template-detail.html',
                    controller: 'NotificationTemplateDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'NotificationTemplate', function($stateParams, NotificationTemplate) {
                    return NotificationTemplate.get({id : $stateParams.id});
                }]
            }
        })
        .state('notification-template.new', {
            parent: 'notification-template',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/notification-template/notification-template-dialog.html',
                    controller: 'NotificationTemplateDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                notificationTemplateId: null,
                                notificationTemplate: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('notification-template', null, { reload: true });
                }, function() {
                    $state.go('notification-template');
                });
            }]
        })
        .state('notification-template.edit', {
            parent: 'notification-template',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/notification-template/notification-template-dialog.html',
                    controller: 'NotificationTemplateDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['NotificationTemplate', function(NotificationTemplate) {
                            return NotificationTemplate.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('notification-template', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('notification-template.delete', {
            parent: 'notification-template',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/notification-template/notification-template-delete-dialog.html',
                    controller: 'NotificationTemplateDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['NotificationTemplate', function(NotificationTemplate) {
                            return NotificationTemplate.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('notification-template', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
