'use strict';

describe('Controller Tests', function() {

    describe('NotificationChannel Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockNotificationChannel, MockNotificationTemplate, MockNotificationEventReminder;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockNotificationChannel = jasmine.createSpy('MockNotificationChannel');
            MockNotificationTemplate = jasmine.createSpy('MockNotificationTemplate');
            MockNotificationEventReminder = jasmine.createSpy('MockNotificationEventReminder');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'NotificationChannel': MockNotificationChannel,
                'NotificationTemplate': MockNotificationTemplate,
                'NotificationEventReminder': MockNotificationEventReminder
            };
            createController = function() {
                $injector.get('$controller')("NotificationChannelDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'notificationApp:notificationChannelUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
