import Data.List
import Data.Fixed

-- P69 E1 E2
length2 :: [a] -> Int
length2 (x:xs) = length2 (xs) + 1 
length2 [] = 0

main = interact wordCount
  where wordCount input = show(length2(input)) ++ "\n"

length3 :: [a] -> Int
length3 (x:xs)
     | null xs   = 1
     | otherwise = 1 + length3 xs

-- P69 E3
mean2 :: [Double] -> Double
mean2 xs = (sum xs) / fromIntegral(length xs)

invert :: [a] -> [a]
invert xs = if (length xs) == 1
            then xs
            else invert (tail xs) ++ take 1 xs

-- P69 E4 E5
palindrome :: [a] -> [a]
palindrome xs = xs ++ invert xs

firstHalf :: [a] -> [a]
firstHalf xs = take (div (length xs) 2) xs

secondHalf :: [a] -> [a]
secondHalf xs = drop (div (length xs) 2) xs

-- backticks
isPalindrome xs = all (`elem` (firstHalf xs)) (invert (secondHalf xs))

-- P70 E6
compareList :: [a] -> [a] -> Ordering
compareList xs1 xs2 
     | (length xs1)  > (length xs2) = GT
     | (length xs1) == (length xs2) = EQ
     | (length xs1) <  (length xs2) = LT

sortSubList :: [[a]] -> [[a]]
sortSubList xss = sortBy compareList xss

-- P70 E7 E8
intersperse :: a -> [[a]] -> [a]
intersperse sp xss
   | null xss        = []						
   | length xss == 1 = head xss 
   | otherwise       = head xss ++ (replicate 1 sp) ++ intersperse sp (tail xss)

-- P70 E9
-- @tree.hs

-- P70 E10 E11
-- @direction.hs

-- P70 E12
-- @convexHull.hs
