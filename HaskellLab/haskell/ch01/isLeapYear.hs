-- to decide on whether the input is a leap year
f :: Int -> Bool
f x = (((mod x 4) == 0) && ((mod x 100) /= 0)) || ((mod x 400) == 0)

  