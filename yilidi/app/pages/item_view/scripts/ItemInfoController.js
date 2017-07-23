/**
 * Author: Shawn, 02.05.2015
 **/
'use strict';

yilidi.controller('ItemInfoCtrl', ['$timeout', '$interval', '$scope', '$routeParams','Item',
    function($timeout, $interval, $scope, $routeParams, Item) {
        Item.onNewItem(function(item) {
            $scope.item = item;
            var curTime = getCurrentTime();
            console.log(item.curPrice);
            if (curTime < item.endTime) {
                var leftTime = item.endTime - curTime;
                addFlipClock(leftTime);
                addFlipPrice(item.curPrice);
            } else {
                addFlipClock(0);
                addFlipPrice(item.curPrice);
            }

            // check item status every 5 seconds
            $interval(function() {
                    Item.getItem(item.id).then(function (newItem) {
                        if (newItem.boughtTime !== null) {
                            $scope.flipClock.time = 0;
                            $scope.flipPrice.value = 0;
                        } else {
                            $scope.flipPrice.value = newItem.curPrice;
                        }
                    })},
                5000);
        });

        var addFlipClock = function(time){
            $scope.flipClock = {
                options: {
                    countdown: true,
                    clockFace: 'DailyCounter',
                    language: "zh-cn",
                    callbacks: {}
                },
                time: time
            };
        };

        var addFlipPrice = function(maxPrice) {
            $scope.flipPrice = {
                options: {
                    clockFace: 'Counter'
                },
                value: maxPrice
            };
        };

        $scope.onBid = function(item, userId, bidPrice) {
            var bid = {
                itemId: item.itemId,
                userId: userId,
                minPrice: item.minPrice,
                maxPrice: item.maxPrice,
                curBidPrice: item.bidPrice,
                newBidPrice: bidPrice,
                newBidDuration: priceRangeToTimeDuration(bidPrice - item.minPrice),
                bidTime: new Date().getTime()
            };
            Item.bidItem(bid);
        };

        $scope.onFavorite = function(itemId, userId) {
            Item.favItem(itemId, userId);
        }

    }]);
