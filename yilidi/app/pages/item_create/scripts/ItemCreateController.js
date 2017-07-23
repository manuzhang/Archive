'use strict';

yilidi.controller('ItemCreateCtrl', ['$scope', '$rootScope', '$timeout', '$mdToast', 'ItemCreate', 'FileUpload', 'Category', 'ITEM_CREATE_DIR',
    function($scope, $rootScope, $timeout, $mdToast, ItemCreate, FileUpload, Category, ITEM_CREATE_DIR) {
        $scope.item = {};

        Category.getCategories(function(categories) {
            $scope.categories = categories;
        });

        $scope.step = 0;

        $scope.slides = [
            {title: '定义你的商品', url: ITEM_CREATE_DIR + '/item-create-step1.html', button: ""},
            {title: '定义你的游戏规则', url: ITEM_CREATE_DIR + '/item-create-step2.html', button: ""},
            {title: '上传照片', url: ITEM_CREATE_DIR + '/item-create-step3.html', button: ITEM_CREATE_DIR + '/item-create-step3-upload.html'}
       //     {title: '预览', url: ITEM_CREATE_DIR + '/item-create-step4.html', button: ""}
        ];

        $scope.lastStep = function() {
            $scope.step--;
        };

        $scope.nextStep = function() {
            $scope.step++;
        };

        $scope.verifyAndNextStep = function(minPrice, maxPrice) {
            console.log(minPrice);
            console.log(maxPrice);
            if (minPrice.parseInt >= maxPrice.parseInt) {
                $mdToast.show(
                    $mdToast.simple()
                        .content('最高价格必须大于最低价格')
                        .position('top')
                        .hideDelay(3000)
                );
            } else {
                $scope.nextStep();
            }
        };

        $scope.getProgress = function() {
            return ($scope.step) / ($scope.slides.length - 1) * 100;
        };

        $scope.submit = function() {
            ItemCreate.createItem($scope.item, $rootScope.currentUser).then(function(response) {
                if (response === "200") {
                    $mdToast.simple()
                        .content('创建成功')
                        .position('left')
                        .hideDelay(3000);
                }
                console.log(response);
                window.location = '/';
            });
        };

        $scope.condition = [
            {value: 1, name: "二手"},
            {value: 0, name: "新品"}
        ];

        $scope.images = [
        ];

        $scope.fileUpload = {
            url: FileUpload.getURL(),
            options: {
                browse_button: "pickfiles",
                max_file_size: "4mb"
            },
            callbacks: {
                init: function(uploader) {
                    FileUpload.getKey();
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
                        {token: FileUpload.getToken(file)});
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
                    ItemCreate.savePicKey(key);
                     $mdToast.simple()
                        .content('图片上传成功')
                        .position('top')
                        .hideDelay(3000);
                },
                error: function(uploader, error) {
                    $scope.loading = false;
                    alert(error.message);
                }
            }
        }

    }]);
