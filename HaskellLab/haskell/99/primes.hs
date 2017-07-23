import Data.Array.Unboxed

-- genuine yet wasteful sieve of Eratosthenes
primesTo m = 2 : eratos [3,5..m] 
  where
    eratos []     = []
    eratos (p:xs) = p : eratos (xs `minus` [p, p+2*p..m])
                              
primesTo2 m = 2 : eulers [3,5..m]
  where
    eulers []     = []
    eulers (p:xs) = p : eulers (xs `minus` map (p*) (p:xs))

primesTo3 m = 2 : turner [3,5..m]
  where
    turner []     = []
    turner (p:xs) = p : turner [x | x <- xs, rem x p /= 0]

-- start each step at a prime's square 
-- smalller multiples will have been already produced on previous steps
primesToQ m = 2 : sieve [3,5..m] 
  where
    sieve []      = []
    sieve (p:xs)  = p : sieve (xs `minus` [p*p, p*p+2*p..m])
    
primesToG m = 2 : sieve [3,5..m]
  where 
    sieve (p:xs)
       | p*p > m   = p : xs
       | otherwise = p : sieve (xs `minus` [p*p, p*p+2*p..m])
                     
minus (x:xs) (y:ys) = case (compare x y) of 
  LT -> x : minus xs (y:ys)
  EQ ->     minus xs ys
  GT ->     minus (x:xs) ys
  
minus xs     _      = xs

-- compiler optimizations
{-# OPTIONS_GHC -o2 #-}
primesToA :: Int -> [Int]
primesToA m = sieve 3 (array (3,m) [(i, odd i) | i <- [3..m]] 
                       :: UArray Int Bool)
  where 
    sieve p a 
      | p*p > m   = 2 : [i | (i, True) <- assocs a]
      | a!p       = sieve (p+2) $ a//[(i, False) | i <- [p*p, p*p+2*p..m]]
      | otherwise = sieve (p+2) a

-- just cannot understand
primesPE = 2 : primes'
  where 
    primes' = sieve [3,5..] 9 primes'
    sieve (x:xs) q ps@ ~(p:t) 
      | x < q     = x : sieve xs q ps
      | otherwise =     sieve (xs `minus` [q, q+2*p..]) (head t^2) t
       