/**
 * convert price range to time duration
 * e.g. 10RMB ~ 100RMB => 90 * 10000 * 1000 milliseconds ~= 10days
 */
function priceRangeToTimeDuration(priceRange) {
    return priceRange * getScale(priceRange) * 1000;
}

function getScale(priceRange) {
    if (priceRange < 100) {
        return 10000;
    } else if (priceRange < 1000) {
        return 1000;
    } else if (priceRange < 10000) {
        return 100;
    } else if (priceRange < 100000) {
        return 10;
    } else {
        return 1;
    }
}
function getCurrentTime() {
    return new Date().getTime();
}

