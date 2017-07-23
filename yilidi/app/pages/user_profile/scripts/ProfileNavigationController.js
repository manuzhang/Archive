'use strict';

yilidi.controller('ProfileNavCtrl', ['$scope', 'USER_PROFILE_DIR',
    function($scope, USER_PROFILE_DIR) {
   $scope.tabs= [
       {title: '用户信息', url: USER_PROFILE_DIR + 'user-profile.html'},
       {title: '账户安全', url: USER_PROFILE_DIR + 'user-security.html'}
      // {title: '已买商品', url: USER_PROFILE_DIR + 'user-bought.html'},
      // {title: '已卖商品', url: USER_PROFILE_DIR + 'user-sold.html'}
   ];
}]);
