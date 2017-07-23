/**
 * Created by shawnfan on 2/8/15.
 */

//Retrieve data from DB and store in sharedProperty service
yilidi.controller('ItemViewCtrl', ['$scope', '$routeParams', 'Item',
    function($scope, $routeParams, Item) {
        var itemId = $routeParams.id;
        Item.getItem(itemId).then(function(ret) {
            //Use $scope.product instead of this.product here. Reason: .then() is a delayed $promise
            Item.setSharedItem(ret);
        });
}]);
