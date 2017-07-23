-- a comparison between tuple and currying
larger :: (Int, Int) -> Int
larger (a, b) =  if a >= b
                 then a
                 else b
-- in Haskell, all functions take only one argument
-- larger2 a b is actually (larger2 a) b 
larger2 :: Int -> Int -> Int
larger2 a b | a >= b      = a
            | otherwise   = b
