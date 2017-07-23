'use strict';

yilidi.controller('AuthCtrl', ['$scope', '$rootScope', '$location', '$cookieStore', 'COOKIE_USER',
    function($scope, $rootScope, $location, $cookieStore, COOKIE_USER) {
    $scope.loggedIn = function() {
        $rootScope.currentUser = $cookieStore.get(COOKIE_USER);
        return is.existy($rootScope.currentUser);
    };

    $scope.logout = function() {
        $rootScope.currentUser = null;
        $cookieStore.remove(COOKIE_USER);
        $location.path('/');
    };

}]);


