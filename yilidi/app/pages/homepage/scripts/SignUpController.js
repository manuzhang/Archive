'use strict';

yilidi.controller('SignUpCtrl', ['$scope', '$rootScope', 'Auth',
    function($scope, $rootScope, Auth) {
        $scope.user = {
            email: "",
            name: "",
            password: ""
        };

        $scope.signUp = function(user) {
            Auth.$createUser(user).then(function(authData) {
                $rootScope.currentUser = authData.uid;
                window.location = "/";
            }).catch(function(error) {
                console.error("Authentication failed: ", error)
            });
        };
    }
]);

