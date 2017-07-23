/**
 * Author: Shawn, 02.05.2015
 **/
'use strict';

yilidi.controller('ItemCommentCtrl', ['$scope', '$routeParams', 'Item','$rootScope',
    function($scope, $routeParams, Item, $rootScope){
        Item.onNewItem(function(item) {
            $scope.comments = item.comments;
        });

        $scope.addComment = function() {
            var date = new Date();//yyyy-mm-dd hh:mm:ss
            var commentCreateDate = date.getFullYear() + "-" + (date.getMonth() + 1) + "-" + date.getDate() + " "
                + date.getHours() + ":" + date.getMinutes() + ":" + date.getSeconds();
            $scope.newComment.commentItemId = $routeParams.id;
            $scope.newComment.commentCreateDate = commentCreateDate;
            $scope.newComment.commentCreateUserId = $rootScope.currentUser.userId;
            $scope.newComment.commentCreateUserNickName = $rootScope.currentUser.nickName;
            $scope.newComment.commentCreateUserPortraitUrl = $rootScope.currentUser.portraitUrl;
            $scope.comments.push($scope.newComment);
            //Store comment to db
            Item.createComment($scope.newComment);
        };
    }]);
