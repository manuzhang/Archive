interval :: (Ord a, RealFrac a) => a -> a -> [a]
interval m n
     | m > (n + 0.5) = []
     | otherwise     = m : interval (m + 1) n
                   
inits :: String -> [String]
inits xs 
   | null xs   = [""]
   | otherwise = inits (init xs) ++ [xs]

