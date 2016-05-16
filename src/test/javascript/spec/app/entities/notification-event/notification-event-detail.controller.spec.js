'use strict';

describe('Controller Tests', function() {

    describe('NotificationEvent Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockNotificationEvent, MockNotificationTemplate;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockNotificationEvent = jasmine.createSpy('MockNotificationEvent');
            MockNotificationTemplate = jasmine.createSpy('MockNotificationTemplate');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'NotificationEvent': MockNotificationEvent,
                'NotificationTemplate': MockNotificationTemplate
            };
            createController = function() {
                $injector.get('$controller')("NotificationEventDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'notificationApp:notificationEventUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
