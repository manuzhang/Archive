data Shape = Circle Float |
             Rectangle Float Float
             
circumference :: Shape -> Float
circumference (Circle r)      = 2 * pi * r
circumference (Rectangle h w) = 2 * (h + w)