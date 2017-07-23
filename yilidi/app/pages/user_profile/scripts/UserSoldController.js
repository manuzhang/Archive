'use strict';

yilidi.controller('UserSoldCtrl', ['$scope', '$rootScope', 'UserProfileService',
    function($scope, $rootScope, UserProfileService) {
            UserProfileService.getUserSoldItems($rootScope.currentUser.userId).then(function(ret){
                $scope.soldItems = ret.entities;
            });
}]);
