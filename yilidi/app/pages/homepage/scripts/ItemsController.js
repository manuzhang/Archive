'use strict';

yilidi.controller('ItemsCtrl', ['$scope', '$routeParams', '$firebaseArray', 'FIREBASE',
    function($scope, $routeParams, $firebaseArray, FIREBASE) {
        var queryParams = {};
        if ($routeParams.category) {
            queryParams = {category: $routeParams.category};
        }

        var itemsRef = new Firebase(FIREBASE + "/messages");
        var items = $firebaseArray(itemsRef);

        items.$loaded(function(ret) {
            $scope.items = ret;
        }, function(error) {
            console.error("Error:", error);
        });

        var favBtnColor = {color: 'grey'};
        $scope.setFavBtnColor = function() {
            favBtnColor.color = 'red';
        };
        $scope.getFavBtnColor = function() {
            return favBtnColor;
        }
}]);


