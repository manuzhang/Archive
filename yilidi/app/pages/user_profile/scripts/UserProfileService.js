'use strict';

yilidi.service('UserProfileService', ['$sanitize', 'Restangular', 'REST_URL', function($sanitize, Restangular, REST_URL) {
    var service = this;
    var ENDPOINT_URL = REST_URL;

    service.updatePassword = function(userId,password){
        var userPasswordData;
        userPasswordData = {
            userId: userId,
            password: $sanitize(password)

        };
        return Restangular.all(ENDPOINT_URL + "/updatePassword").post(userPasswordData);
    };

    service.saveUserProfile = function(userProfile) {
        var userData;
        userData = {
            userId: userProfile.userId,
            name: userProfile.name,
            nickName:userProfile.nickName,
            mobile: userProfile.mobile,
            portraitUrl: userProfile.portraitUrl
        };
        console.log(userData);
        return Restangular.all(ENDPOINT_URL + "/user/saveProfile").post(userData);
    };

    service.updatePassword = function(userId,password) {
        var userProfileData;
        userProfileData = {
            userId: userId,
            password: $sanitize(password)

        };
        return Restangular.all(ENDPOINT_URL + "/user/updatePassword").post(userProfileData);
    };

    service.updateEmail = function(userId,email) {
        console.log("enter into updatemail");
        var userProfileData;
        userProfileData = {
            userId: userId,
            email: email

        };
        console.log(userProfileData);
        return Restangular.all(ENDPOINT_URL + "/user/updateEmail").post(userProfileData);
    };

    service.getUserProfile = function(userId) {
        return Restangular.one(ENDPOINT_URL + "/user/" + userId).get();
    };

    service.getUserSoldItems = function(userId) {
        return Restangular.one(ENDPOINT_URL + "/item/my-selling-items/" + userId).get();
    };

    service.getUserBoughtItems = function(userId) {
        return Restangular.one(ENDPOINT_URL + "/item/my-bought-items/" + userId).get();
    };

//    service.getUserCollectionItems = function(userId) {
//        return Restangular.one(ENDPOINT_URL + "/item/collection", userId).get();
//    }


    var phoneNumberRegex = /^\d{11}$/;
    var isPhoneNumber = function(account) {
        return account.match(phoneNumberRegex);
    };
}]);

