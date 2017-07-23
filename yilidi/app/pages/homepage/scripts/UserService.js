'use strict';

yilidi.factory("Auth", ["$firebaseAuth", "FIREBASE",
    function($firebaseAuth, FIREBASE) {
        var ref = new Firebase(FIREBASE);
        return $firebaseAuth(ref);
    }
]);
