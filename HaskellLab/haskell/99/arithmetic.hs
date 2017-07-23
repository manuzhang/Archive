import Data.List(nub)
import Data.Ratio

-- determine whether a given number is prime
isPrime :: Integer -> Bool
isPrime n 
  | n <= 1     = False
  | n == 2     = True
  | otherwise  = all (/=0) $  map (mod n) $ getList n 2
     where getList n i 
             | i * i >= n = []
             | otherwise  = i : getList n (i+1)
-------------------------------------------------------------
-- determine the greatest common divisor of two positive integer numbers
-- using Euclid's algorithm
           
myGCD :: Integer -> Integer -> Integer
myGCD x 0 = x
myGCD x y 
  | x < 0 || y < 0 = myGCD (abs x) (abs y)
  | x >= y         = myGCD y (mod x y)
  | x < y          = myGCD y x
                     
myGCD2 a b
   | b == 0    = abs a
   | otherwise = myGCD2 b (a `mod` b)

-- determine whether two positive integer numbers are coprime
isCoprime :: Integer -> Integer -> Bool
isCoprime x y = myGCD x y == 1 
---------------------------------------------------------------
-- phi(m), that is, number of positive integers r (1 <= r < m) 
-- that are coprime to m
totient :: Integer -> Integer
totient 1 = 1
totient m = sum $ map (\x -> if isCoprime x m then 1 else 0) [1..m]


totient2 m = length [x | x <- [1..m], isCoprime x m]

totient3 :: Integer -> Integer
totient3 1 = 1
totient3 n = numerator ratio `div` denominator ratio
-- nub removes duplicate elements from a list
   where ratio = foldl (\acc x -> acc*(1-(1%x))) (n%1) $ nub (primeFactors n)

-- primeFactors  315
-- [3, 3, 5, 7]
primeFactors :: Integer -> [Integer]
primeFactors n 
  | n <= 1      = []
  | otherwise   = factor : primeFactors (div n factor)
                     where factor = getFactor n 2
                           getFactor :: Integer -> Integer -> Integer
                           getFactor i x
                               | mod i x == 0 && isPrime x = x
                               | otherwise                 = getFactor i (x+1)
                                                             
-- prime_factors_mult 315
-- [(3,2),(5,1),(7,1)]
prime_factors_mult :: Integer -> [(Integer, Integer)]
prime_factors_mult n = foldl f [] (primeFactors n)
                       where f :: [(Integer, Integer)] -> Integer -> [(Integer, Integer)]
                             f acc i 
                               | acc == []   = [(i,1)]
                               | factor == i = (init acc) ++ [(factor, mult+1)]
                               | factor /= i = acc ++ [(i,1)]
                                  where factor = fst . last $ acc
                                        mult   = snd . last $ acc
-- phi(315) = (3-1)*3**(2-1) * (5-1)*5**(1-1) * (7-1)*7**(1-1) = 144                                        
improvedTotient :: [(Integer, Integer)] -> Integer
improvedTotient = foldl (\acc (p,m)-> acc*(p-1)*p^(m-1)) 1 

improvedTotient2 :: Integer -> Integer
improvedTotient2 m = product [(p-1)*p^(c-1) | (p,c) <- prime_factors_mult m]

-- a list of prime numbers given upper and lower limit
primesR :: Integer -> Integer -> [Integer]
primesR x y 
  | x <= y  = filter (/=0) $ map (\w -> if isPrime w then w else 0) [x..y]
  | x >  y = primesR y x
                  
----------------------------------------------------------------------------
-- Goldbach's conjecture
-- every positive even number greater than 2 is the sum of two prime numbers
goldbach :: Integer -> (Integer, Integer)
goldbach n
  | mod n 2 /= 0 = (0,0)
  | otherwise    = head . filter (/=(0,0)) $ map (\x -> if isPrime x && isPrime (n-x) then (x,n-x) else (0,0)) [1,3..(div n 2)]

goldbachList :: Integer -> Integer -> [(Integer, Integer)]
goldbachList x y
  | x <= y = filter (/=(0,0)) $ map goldbach [x..y]
  | x >  y = goldbachList y x
             
goldbachList' :: Integer -> Integer -> Integer -> [(Integer, Integer)]
goldbachList' x y limit = filter (\(a,b) -> a >= limit && b >= limit) $ goldbachList x y

goldbachList2 n m = map goldbach $ dropWhile (<4) $ filter even [n..m]
goldbachList2' n m i = filter (\(x,y) -> x >i && y > i) $ goldbachList2 n m