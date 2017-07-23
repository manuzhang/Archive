/**
 * Author: Shawn, 02.05.2015
 **/
'use strict';

yilidi.controller('ItemImgCtrl', ["$scope", 'Item', function($scope, Item) {
    Item.onNewItem(function(item) {
        var id = 0;
        $scope.slides = [];
        angular.forEach(item.pictures, function(url) {
            $scope.slides.push({id: id, url: url});
            id += 1;
        });
    });
}]);

