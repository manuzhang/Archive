'use strict';

yilidi.service('ItemCreate', ['$firebaseArray', 'REST_URL', 'PIC_STORE_URL', 'FIREBASE',
    function($firebaseArray, REST_URL, PIC_STORE_URL, FIREBASE) {
    var service = this;
    var ENDPOINT_URL = REST_URL + "/item";

    var pictures = [];

    service.createItem = function(item, user) {
        item["pictures"] = pictures;
        item["seller"] = user;
        item["startTime"] = new Date().getTime();
        var timeDuration = priceRangeToTimeDuration(item.maxPrice - item.minPrice);
        item["endTime"] =  item["startTime" ] + timeDuration;
        console.log(item);

        var messagesRef = new Firebase(FIREBASE + "/messages");
        var messages = $firebaseArray(messagesRef);
        return messages.$add(item);
    };


    service.savePicKey = function(picKey) {
        pictures.push(PIC_STORE_URL + picKey);
    }
}]);
