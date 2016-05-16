'use strict';

describe('Controller Tests', function() {

    describe('NotificationTemplate Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockNotificationTemplate, MockNotificationChannel, MockNotificationEvent, MockNotificationNotifieeType, MockOrganization;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockNotificationTemplate = jasmine.createSpy('MockNotificationTemplate');
            MockNotificationChannel = jasmine.createSpy('MockNotificationChannel');
            MockNotificationEvent = jasmine.createSpy('MockNotificationEvent');
            MockNotificationNotifieeType = jasmine.createSpy('MockNotificationNotifieeType');
            MockOrganization = jasmine.createSpy('MockOrganization');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'NotificationTemplate': MockNotificationTemplate,
                'NotificationChannel': MockNotificationChannel,
                'NotificationEvent': MockNotificationEvent,
                'NotificationNotifieeType': MockNotificationNotifieeType,
                'Organization': MockOrganization
            };
            createController = function() {
                $injector.get('$controller')("NotificationTemplateDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'notificationApp:notificationTemplateUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
