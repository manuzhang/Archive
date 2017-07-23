'use strict';

yilidi.service('Category', ['Restangular', 'REST_URL', function(Restangular, REST_URL) {
    var service = this;
    var ENDPOINT_URL = REST_URL + "/category";
    service.categories = [];

    service.getCategories = function(callback) {
        if (is.empty(service.categories)) {
            Restangular.one(ENDPOINT_URL).get().then(function(ret) {
                angular.forEach(ret.entities, function(category) {
                    service.categories.push(category);
                });
                callback(service.categories);
            });
        } else {
            callback(service.categories);
        }
    };

}]);
