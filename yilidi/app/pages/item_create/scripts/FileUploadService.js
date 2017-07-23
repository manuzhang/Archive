"use strict";

yilidi.service("FileUpload", ['Restangular', 'REST_URL',
    function(Restangular, REST_URL) {
    var service = this;
    var KEY_URL = REST_URL + "/file/keys";
    var UPLOAD_URL = "http://upload.qiniu.com";

    var accessKey = "";
    var secretKey = "";
    var token = "";

    service.getURL = function() {
        return UPLOAD_URL;
    };

    service.getKey = function() {
        if (accessKey === "" || secretKey === "") {
            getKey().then(function (ret) {
                accessKey = ret.accessKey;
                secretKey = ret.secretKey;
            });
        }
    };

    service.getToken = function(file) {
        if (token === "") {
           token = initToken(file);
        }
        return token;
    };

    var initToken = function(file) {
        var policy = JSON.stringify(generatePolicy(file));
        var encoded = base64encode(policy);
        var hash = CryptoJS.HmacSHA1(encoded, secretKey);
        var encoded_signed = hash.toString(CryptoJS.enc.Base64);
        var upload_token =
            accessKey + ":" +
            safe64(encoded_signed) + ":" +
            encoded;
        return upload_token;
    };

    var getKey = function() {
        return Restangular.one(KEY_URL).get()
    };

    var generatePolicy = function(file) {
        var policy = {
            scope: "yilidi",
 //           saveKey: file.name,
            deadline: parseInt(new Date().getTime()/1000) + 3600 * 24 * 365
 /*           returnBody: '{' +
            '"name": $(fname),'+
            '"size": $(fsize),'+
            '"w": $(imageInfo.width),' +
            '"h": $(imageInfo.height),' +
            '"hash": $(etag)}'*/
        };
        return policy;
    };

}]);
