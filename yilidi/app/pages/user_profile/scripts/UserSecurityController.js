'use strict';

yilidi.controller('UserSecurityCtrl', ['$scope', '$rootScope', '$cookieStore',
    '$mdToast', '$location', 'COOKIE_USER', 'UserProfileService',
    function($scope, $rootScope, $cookieStore, $mdToast, $location, COOKIE_USER, UserProfileService) {
        $scope.currentPassword = null;
        $scope.emailPassword = null;
        $scope.newPassword = null;
        $scope.confirmPassword = null;
        $scope.userEmail = $rootScope.currentUser.email;


        var logout = function() {
            $rootScope.currentUser = null;
            $cookieStore.remove(COOKIE_USER);
            $location.path('/login');
        };

        $scope.updatePassword = function () {
            if ($scope.currentPassword !== $rootScope.currentUser.password) {
                console.log($scope.currentPassword);
                console.log($scope.currentUser);
                $mdToast.show(
                    $mdToast.simple()
                        .content('当前密码错误')
                        .position('top')
                        .hideDelay(3000)
                );
            } else if ($scope.newPassword !== $scope.confirmPassword) {
                $mdToast.show(
                    $mdToast.simple()
                        .content('重复密码不一致')
                        .position('top')
                        .hideDelay(3000)
                );
            } else {
                UserProfileService.updatePassword($rootScope.currentUser.userId, $scope.newPassword).then(function (response) {
                    $mdToast.show(
                        $mdToast.simple()
                            .content('密码修改成功, 请重新登录')
                            .position('top')
                            .hideDelay(3000)
                    );
                    logout();
                });
            }
        };

        $scope.updateEmail = function () {
            if ($scope.currentUser.password === $scope.emailPassword) {
                UserProfileService.updateEmail($rootScope.currentUser.userId, $scope.userEmail).then(function (response) {
                    $mdToast.show(
                        $mdToast.simple()
                            .content('邮箱修改成功, 请重新登录')
                            .position('top')
                            .hideDelay(3000));
                });
                logout();
            } else {
                $mdToast.show(
                    $mdToast.simple()
                        .content('密码错误')
                        .position('top')
                        .hideDelay(3000));
            }

        };
    }]);
