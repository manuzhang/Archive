'use strict';

yilidi.service('Item', ['Restangular', 'REST_URL', function(Restangular, REST_URL) {
    var service = this;
    var ENDPOINT_URL = REST_URL + "/item";
    var sharedItem = "";
    var callbacks = [];

    service.onNewItem = function(cb) {
        callbacks.push(cb);
    };

    service.getSharedItem = function(){
        return sharedItem;
    };

    service.setSharedItem = function(value) {
        sharedItem = value;
        angular.forEach(callbacks, function(cb) {
            cb(sharedItem);
        });
    };

    service.setCategory = function(category) {
        service.category = category;
    };

    service.getItem = function(id) {
        return Restangular.one(ENDPOINT_URL, id).get();
    };

    service.bidItem = function(bid) {
        return Restangular.all(ENDPOINT_URL + "/bid").post(bid);
    };

    service.favItem = function(itemId, userId) {
        var fav = {
            itemId: itemId,
            userId: userId
        };
        return Restangular.all(ENDPOINT_URL + "/fav").post(fav);
    };

    service.createComment = function(comment) {
        comment["commentReplyUserId"] = "";//TODO Need update with a list of commentReplyId's
        Restangular.all(ENDPOINT_URL + "/comment").post(comment);
    };

    service.addCommentCreator = function(index, userProfile) {
        sharedItem.itemComments[index]["userProfile"] = userProfile;
    };
    service.markCommentCreator = function(index) {
        sharedItem.itemComments[index].userProfileAdded = true;
    };


    service.gradeItem = function(postParam) {
        Restangular.post(ENDPOINT_URL + "/grade", postParam)
    };

    service.gradeItemWithLike = function() {
        Restangular.post(ENDPOINT_URL + "/grade/like");
    };

    service.gradeItemWithPoor = function() {
        Restangular.post(ENDPOINT_URL + "/grade/poor");
    };

    service.gradeItemWithNeutral = function() {
        Restangular.post(ENDPOINT_URL + "/grade/neutral");
    };

    service.getItemLikeCount = function(itemId) {
        Restangular.one(ENDPOINT_URL + "/like", itemId);
    };

    service.collectItem = function(postParam) {
        Restangular.post(ENDPOINT_URL + "/collect", postParam);
    };

    service.getItemCollectCount = function(itemId) {
        Restangular.one(ENDPOINT_URL + "/collection", itemId);
    };

    service.getItemStatus = function(itemId) {
        Restangular.one(ENDPOINT_URL + "/status", itemId);
    };

    service.setItemStatus = function(itemId, status) {
        if (status === "accept" || status === "reject" || status === "reserve" || status === "success") {
            Restangular.post(ENDPOINT_URL + "/status/" + status, {itemId: itemId});
        } else {
            alert("invalid status");
        }
    };
}]);

