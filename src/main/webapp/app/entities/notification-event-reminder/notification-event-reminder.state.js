(function() {
    'use strict';

    angular
        .module('notificationApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('notification-event-reminder', {
            parent: 'entity',
            url: '/notification-event-reminder',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'NotificationEventReminders'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/notification-event-reminder/notification-event-reminders.html',
                    controller: 'NotificationEventReminderController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
            }
        })
        .state('notification-event-reminder-detail', {
            parent: 'entity',
            url: '/notification-event-reminder/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'NotificationEventReminder'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/notification-event-reminder/notification-event-reminder-detail.html',
                    controller: 'NotificationEventReminderDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'NotificationEventReminder', function($stateParams, NotificationEventReminder) {
                    return NotificationEventReminder.get({id : $stateParams.id});
                }]
            }
        })
        .state('notification-event-reminder.new', {
            parent: 'notification-event-reminder',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/notification-event-reminder/notification-event-reminder-dialog.html',
                    controller: 'NotificationEventReminderDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                notificationEventReminderId: null,
                                notificationEventReminderDateTime: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('notification-event-reminder', null, { reload: true });
                }, function() {
                    $state.go('notification-event-reminder');
                });
            }]
        })
        .state('notification-event-reminder.edit', {
            parent: 'notification-event-reminder',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/notification-event-reminder/notification-event-reminder-dialog.html',
                    controller: 'NotificationEventReminderDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['NotificationEventReminder', function(NotificationEventReminder) {
                            return NotificationEventReminder.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('notification-event-reminder', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('notification-event-reminder.delete', {
            parent: 'notification-event-reminder',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/notification-event-reminder/notification-event-reminder-delete-dialog.html',
                    controller: 'NotificationEventReminderDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['NotificationEventReminder', function(NotificationEventReminder) {
                            return NotificationEventReminder.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('notification-event-reminder', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
