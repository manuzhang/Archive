'use strict';

yilidi.controller('UserBoughtCtrl', ['$scope', '$rootScope', 'UserProfileService',
    function($scope, $rootScope, UserProfileService) {
        UserProfileService.getUserBoughtItems($rootScope.currentUser.userId).then(function(ret) {
           $scope.boughtItems = ret.entities;
        });

}]);
