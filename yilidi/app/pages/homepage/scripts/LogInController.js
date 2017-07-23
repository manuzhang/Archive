'use strict';

yilidi.controller('LogInCtrl', ['$scope', '$rootScope', '$cookieStore', '$mdToast', 'Auth', 'COOKIE_USER',
    function($scope, $rootScope, $cookieStore, $mdToast, Auth, COOKIE_USER) {
        $scope.user = {
            email: "",
            password: ""
        };

        $scope.logIn = function(user) {
            Auth.$authWithPassword(user)
                .then(function(authData) {
                    $rootScope.currentUser = authData.uid;
                    console.log(authData);
                    $cookieStore.put(COOKIE_USER, $rootScope.currentUser);
                    window.location = "/";
                }).catch(function(error) {
                    console.error("Authentication failed: ", error)
                });
        };
    }
]);

