/**
 * Created by shawnfan on 4/4/15.
 */
yilidi.controller('ItemSellerCtrl', ['$scope', '$routeParams','Item',
    function($scope, $routeParams, Item){
        Item.onNewItem(function(item) {
            $scope.item = item;
            $scope.userProfile = item.seller;
        });
}]);
