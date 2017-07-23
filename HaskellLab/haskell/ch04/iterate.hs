import Data.Char (isAlphaNum, ord)

generate :: Int -> Int -> Int -> [Int]
generate a b c = takeWhile (< c) (iterate (+(b - a)) 0)