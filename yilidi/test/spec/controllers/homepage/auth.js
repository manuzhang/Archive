'use strict';

describe('Controller: AuthCtrl', function() {
    beforeEach(function(){
        this.addMatchers({
            toEqualData: function(expected) {
                return angular.equals(this.actual, expected);
            }
        });
    });

    // load the controller's module
    beforeEach(module('homepage'));

    var AuthCtrl, scope, $httpBackend;

    // Initialize the controller and a mock scope
    beforeEach(inject(function(_$httpBackend_, $controller, $rootScope) {
        scope = $rootScope.$new();
        scope.user = {
            name: 'foo',
            password: 'bar'
        };
        AuthCtrl = $controller('AuthCtrl', {
            $scope: scope
        });
        $httpBackend = _$httpBackend_;
        $httpBackend
            .expectGet('rest/user/account=foo&pwd=bar')
            .respond({name: 'foo'});

    }));

    it('should create a model with user foo fetched from xhr', function() {
        expect(scope.user).toEqualData({});
        scope.login();
        $httpBackend.flush();

        expect(scope.user).toEqualData(
            {name: 'foo'}
        );
        expect(scope.signedIn()).toEqualData(true);
    });
});
