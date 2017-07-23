'use strict';

var yilidi = angular.module('yilidi', [
    'ngAnimate',
    'ngCookies',
    'ngResource',
    'ngRoute',
    'ngSanitize',
    'angular-carousel',
    'restangular',
    'wu.masonry',
    'angular-plupload',
    'angular-flexslider',
    'ngCookies',
    'ngTable',
    'ngMaterial',
    'ngMdIcons',
    'firebase'
]);

yilidi.config(['$routeProvider', function($routeProvider) {
    $routeProvider
        .when('/', {
            controller: 'ItemsCtrl',
            templateUrl: 'pages/homepage/views/items.html'
        })
        .when('/login', {
            controller: 'LogInCtrl',
            templateUrl: 'pages/homepage/views/login.html'
        })
        .when('/signup', {
            controller: 'SignUpCtrl',
            templateUrl: 'pages/homepage/views/signup.html'
        })
        .when('/:category', {
            controller: 'ItemsCtrl',
            templateUrl: 'pages/homepage/views/items.html'
        })
        .when('/item/create', {
            controller: 'ItemCreateCtrl',
            resolve: {
                currentUser: function($rootScope, $location, $mdToast) {
                    var user = $rootScope.currentUser;
                    console.log(user);
                    if (is.existy(user)) {
                        return user;
                    } else {
                        $mdToast.show(
                            $mdToast.simple()
                                .content("请先登录")
                                .position("top")
                                .hideDelay(3000)
                        );
                        $location.path('/login');
                    }
                }
            },
            templateUrl: 'pages/item_create/views/create-item.html'
        })
        .when('/item/view/:id', {
            templateUrl: 'pages/item_view/views/item.html'
        })
        .when('/user/profile/', {
            controller: 'ProfileNavCtrl',
            templateUrl: 'pages/user_profile/views/profile-nav.html'
        })
        .otherwise({
            redirectTo: '/'
        });
}]);

yilidi.config(function($mdThemingProvider) {
    $mdThemingProvider.theme('default')
        .primaryPalette('blue')
        .accentPalette('orange');
});

yilidi.constant('FIREBASE', "https://yilidi.firebaseio.com");

yilidi.constant('REST_URL', "rest");

yilidi.constant('COOKIE_USER', 'current_user');

yilidi.constant('ITEM_CREATE_DIR', 'pages/item_create/views/');

yilidi.constant('USER_PROFILE_DIR', 'pages/user_profile/views/');

yilidi.constant('PIC_STORE_URL','http://7u2mx4.com1.z0.glb.clouddn.com/');
