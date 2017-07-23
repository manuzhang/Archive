import Data.Function (fix) 
import Data.List (unfoldr)
fib :: Int -> Int
fib n 
  | n == 1    = 0 
  | n == 2    = 1
  | otherwise = fib (n - 2) + fib (n - 1)
                
fibList :: Int -> [Int]
fibList n 
  | n == 1    = [fib 1]
  | otherwise = fibList (n - 1) ++ [fib n]

fibs :: [Int]
fibs = map fib [1..]

f = 0:1:zipWith (+) f (tail f)
f2 = scanl (+) 0 (1:f2)
f3 = 0:scanl (+) 1 f3
f4 = fix (scanl (+) 0 . (1:))
f5 = unfoldr (\(a,b) -> Just (a, (b,a+b))) (0,1)
f6 = map fst $ iterate (\(a,b) -> (b,a+b)) (0,1)