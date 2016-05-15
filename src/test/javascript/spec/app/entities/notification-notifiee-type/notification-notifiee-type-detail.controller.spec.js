'use strict';

describe('Controller Tests', function() {

    describe('NotificationNotifieeType Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockNotificationNotifieeType, MockNotificationTemplate, MockNotificationEventReminder;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockNotificationNotifieeType = jasmine.createSpy('MockNotificationNotifieeType');
            MockNotificationTemplate = jasmine.createSpy('MockNotificationTemplate');
            MockNotificationEventReminder = jasmine.createSpy('MockNotificationEventReminder');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'NotificationNotifieeType': MockNotificationNotifieeType,
                'NotificationTemplate': MockNotificationTemplate,
                'NotificationEventReminder': MockNotificationEventReminder
            };
            createController = function() {
                $injector.get('$controller')("NotificationNotifieeTypeDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'notificationApp:notificationNotifieeTypeUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
