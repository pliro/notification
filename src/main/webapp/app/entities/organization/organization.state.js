(function() {
    'use strict';

    angular
        .module('notificationApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('organization', {
            parent: 'entity',
            url: '/organization',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'Organizations'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/organization/organizations.html',
                    controller: 'OrganizationController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
            }
        })
        .state('organization-detail', {
            parent: 'entity',
            url: '/organization/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'Organization'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/organization/organization-detail.html',
                    controller: 'OrganizationDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'Organization', function($stateParams, Organization) {
                    return Organization.get({id : $stateParams.id});
                }]
            }
        })
        .state('organization.new', {
            parent: 'organization',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/organization/organization-dialog.html',
                    controller: 'OrganizationDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                organizationId: null,
                                organizationName: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('organization', null, { reload: true });
                }, function() {
                    $state.go('organization');
                });
            }]
        })
        .state('organization.edit', {
            parent: 'organization',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/organization/organization-dialog.html',
                    controller: 'OrganizationDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Organization', function(Organization) {
                            return Organization.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('organization', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('organization.delete', {
            parent: 'organization',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/organization/organization-delete-dialog.html',
                    controller: 'OrganizationDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Organization', function(Organization) {
                            return Organization.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('organization', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
