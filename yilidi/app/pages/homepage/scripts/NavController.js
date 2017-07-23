'use strict';

yilidi.controller("NavCtrl", ['$scope', '$mdSidenav', 'Category',
    function($scope, $mdSidenav, Category) {
        $scope.toggleCategoryNav = function() {
            $mdSidenav('category-nav').toggle();
        };

        Category.getCategories(function(categories) {
            $scope.categories = categories;
        });

        $scope.showExpandMoreButton = function(category, open) {
            return category.children.length > 0 && open
        };

        $scope.showExpandLessButton = function(category, open) {
            return category.children.length > 0 && !open
        };

        $scope.getCategoryUrl = function(category) {
            if (category.children.length > 0) {
                return "";
            } else {
                return "#/" + category.categoryId;
            }
        };

        $scope.goHome = function() {
            window.location = "/";
        };

        $scope.goProfile = function() {
            window.locaiton = "#/user/profile";
        };

        $scope.goCreateItem = function() {
            window.location = "#/item/create";
        }
}]);
