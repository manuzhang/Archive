'use strict';

yilidi.controller('UserProfileCtrl', ['$scope', '$mdToast', '$rootScope', '$timeout',
    '$cookieStore', 'PIC_STORE_URL', 'UserProfileService', 'UserPictureUploadService', 'COOKIE_USER',
    function ($scope, $mdToast, $rootScope, $timeout, $cookieStore, PIC_STORE_URL,
              UserProfileService, UserPictureUploadService, COOKIE_USER) {
        $scope.userProfile = $rootScope.currentUser;

        $scope.saving = false;

        $scope.saveProfile = function () {
            $scope.saving = true;
            UserProfileService.saveUserProfile($scope.userProfile).then(function (response) {
                $rootScope.currentUser = response;
                $cookieStore.remove(COOKIE_USER);
                $cookieStore.put(COOKIE_USER, $rootScope.currentUser);
                $scope.saving = false;
                $mdToast.show(
                    $mdToast.simple()
                        .content("修改成功")
                        .position("left")
                        .hideDelay(3000)
                );
            });
        };

        $scope.images = [];

        $scope.userPictureUpload = {
            url: UserPictureUploadService.getURL(),
            options: {
                browse_button: "user_picture_file",
                max_file_size: '4mb'
            },
            callbacks: {
                init: function(uploader) {
                    UserPictureUploadService.getKey();
                    $scope.uploadFiles = function() {
                        uploader.start();
                    }
                },
                filesAdded: function(uploader, files) {
                    var file = files[0];
                    var loader = new mOxie.Image();
                    loader.onload = function() {
                        var url = loader.getAsDataURL();
                        $scope.images.push({url: url});
                    };
                    loader.load(file.getSource());
                    uploader.setOption("multipart_params",
                        {token: UserPictureUploadService.getToken(file)});
                    $timeout(function() {
                        uploader.start();
                    }, 1);
                    $scope.loading = true;
                },
                uploadProgress: function(uploader, file) {
                    $scope.progress = file.percent;
                },
                fileUploaded: function(uploader, file, response) {
                    $scope.loading = false;
                    var key = JSON.parse(response.response).key;
                    $scope.userProfile.userPortraitUrl = PIC_STORE_URL + key;
                    $mdToast.show(
                        $mdToast.simple()
                            .content("上传成功")
                            .position("left")
                            .hideDelay(3000)
                    );
                },
                error: function(uploader, error) {
                    $scope.loading = false;
                    alert(error.message);
                    $mdToast.show(
                        $mdToast.simple()
                            .content("上传失败")
                            .position("left")
                            .hideDelay(3000)
                    );
                }
            }
        }
    }
]);

yilidi.directive('validConfirmPassword', function () {
    return {
        require: 'ngModel',
        link: function (scope, elm, attrs, ctrl) {
            ctrl.$parsers.unshift(function (viewValue, $scope) {
                var noMatch = viewValue != scope.passwordForm.newPassword.$viewValue
                ctrl.$setValidity('noMatch', !noMatch)
            })
        }
    }
});

