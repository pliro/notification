'use strict';

describe('Controller Tests', function() {

    describe('Organization Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockOrganization, MockNotificationTemplate, MockNotificationEventReminder;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockOrganization = jasmine.createSpy('MockOrganization');
            MockNotificationTemplate = jasmine.createSpy('MockNotificationTemplate');
            MockNotificationEventReminder = jasmine.createSpy('MockNotificationEventReminder');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'Organization': MockOrganization,
                'NotificationTemplate': MockNotificationTemplate,
                'NotificationEventReminder': MockNotificationEventReminder
            };
            createController = function() {
                $injector.get('$controller')("OrganizationDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'notificationApp:organizationUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
