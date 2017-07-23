import Data.List (tails)

-- to only returns the nonempty suffixes
suffixes :: [a] -> [[a]]
suffixes xs@(_:xs') = xs : suffixes xs'  -- bind the variable xs to the value 
                                         -- that matches the right side of 
                                         -- @symbol
suffixes _          = []

noAsPattern :: [a] -> [[a]]
noAsPattern (x:xs)  = (x:xs) : noAsPattern xs
noAsPattern _       = []

compose :: (b -> c) -> (a -> b) -> a -> c
compose f g xs = f (g xs)

suffixes2 :: [a] -> [[a]]
suffixes2 = compose init tails

compose2 :: ([[a]] -> [[a]]) -> ([a] -> [[a]]) -> [a] -> [[a]]
compose2 f g xs = f (g xs)

suffixes3 :: [a] -> [[a]]
suffixes3 = init . tails
