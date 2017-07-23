'use strict';

yilidi.directive('flipClock', function() {
    return {
        restrict: 'A',
        scope: {
            options:'=flipClockOptions',
            time:'=flipClockTime'
        },
        link: function(scope, element, attr) {
            var clock = new FlipClock(element, scope.options);
            clock.setTime(scope.time);
            clock.start();
        }
    }
});

yilidi.directive('flipPrice', function($interval) {
    return {
        restrict: 'A',
        scope: {
            options:'=flipPriceOptions',
            price:'=flipPriceValue'
        },
        link: function(scope, element, attr) {
            var clock = new FlipClock(element, scope.price, scope.options);
        }
    }
});
