import Data.Char (isUpper)
max_list :: Ord a => [a] -> Maybe a
max_list [] = Nothing
max_list xs = Just (head (foldr f [] xs))
    where f x ys 
                 | (null ys) || x > (head ys) = [x]
                 | otherwise                  = ys
                 
sum_odd :: [Int] -> Int
sum_odd xs = foldr f 0 xs
    where f x ys
                 | odd x     = ys + x * x
                 | otherwise = ys

sum_even :: [Int] -> Int
sum_even xs = foldr f 0 xs
    where f x ys
                | even x     = ys + x * x
                | otherwise  = ys

sum_all :: [Int] -> Int
sum_all xs = foldr f 0 xs
    where f x ys = ys + x * x
          
upper :: String -> String
upper xs = filter isUpper xs
