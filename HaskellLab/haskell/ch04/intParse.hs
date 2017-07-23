import Data.Char (digitToInt, isDigit)

asInt :: String -> Int
asInt xs = loop 0 xs

loop :: Int -> String -> Int
loop acc [] = acc
loop acc (x:xs) 
  | isDigit x  = let acc' = acc * 10 + digitToInt x
                 in loop acc' xs
  | otherwise  = loop acc xs 