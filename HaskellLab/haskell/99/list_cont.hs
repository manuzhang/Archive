import Data.List(group)

encode :: (Eq a) => [a] -> [(Int, a)]
encode [] = []
encode xs = map (\x -> (length x, head x)) $ group xs

data Code a = Multiple Int a
            | Single a
            deriving (Show)

-- encodeModified "aaaabccaadeeee"
-- [Multiple 4 'a', Single 'b', Multiple 2 'c',
--  Multiple 2 'a', Single 'd', Multiple 4 'e']

encodeModified :: (Eq a) => [a] -> [Code a]
encodeModified [] = []
encodeModified xs = foldl f [] (encode xs)
                     where f acc (ln, hd)  
                             | ln == 1   = acc ++ [Single hd]
                             | otherwise = acc ++ [Multiple ln hd]
                                           
encodeModified2 :: (Eq a) => [a] -> [Code a]
encodeModified2 = map encodeHelper . encode
                  where encodeHelper (1, hd) = Single hd
                        encodeHelper (n, hd) = Multiple n hd
                                                
----------------------------------------------------------------------
getNum :: Code a -> Int
getNum (Multiple n _) = n
getNum (Single _    ) = 1

getHead :: Code a -> a
getHead (Multiple _ x) = x
getHead (Single x    ) = x

toTuple :: Code a -> (Int, a)
toTuple (Single x)     = (1, x)
toTuple (Multiple n x) = (n, x)

decode :: [(Int, a)] -> [a]
decode = concatMap (uncurry replicate)

decodeModified :: [Code a] -> [a]
decodeModified [] = []
decodeModified xs = foldl f [] xs
                     where f acc x = acc ++ (replicate (getNum x) (getHead x))
                                             
decodeModified2 :: [Code a] -> [a]
decodeModified2 = concatMap decodeHelper
     where 
       decodeHelper (Single x)     = [x]
       decodeHelper (Multiple n x) = replicate n x
                           
decodeModified3 :: [Code a] -> [a]
decodeModified3 = concatMap (uncurry replicate . toTuple)

decodeModified4 :: [Code a] -> [a]
decodeModified4 = foldl (\acc e -> case e of Single x -> acc ++ [x]; Multiple n x -> acc ++ replicate n x) []
--------------------------------------------------------------------------      -- no sublists generated now                    
encodeDirect :: (Eq a) => [a] -> [Code a]
encodeDirect = foldl f []
         where 
           f []  x        = [Single x]
           f acc x 
             | l == x    = (init acc) ++ [Multiple (n+1) l] 
             | otherwise = acc ++ [Single x] 
             where l = getHead $ last acc
                   n = getNum $ last acc
                                     
---------------------------------------------------------------------------
                                     
dupli :: [a] -> [a]
dupli [] = []
dupli xs = foldl f [] xs
            where f acc x = acc ++ (replicate 2 x)
                  
dupli2 []     = []
dupli2 (x:xs) = x : x : dupli xs

dupli3 list = concat [[x,x] | x <- list]

dupli4 xs = xs >>= (\x -> [x,x])

dupli5 = concatMap (\x -> [x,x])

dupli6 = concatMap (replicate 2)

--------------------------------------------------------------------------
repli :: [a] -> Int -> [a]
repli [] _ = []
repli xs n = foldl f [] xs 
              where f acc x = acc ++ (replicate n x)
                    
-------------------------------------------------------------------------
dropEvery :: [a] -> Int -> [a]
dropEvery [] _ = []
dropEvery xs n = take (n-1) xs ++ dropEvery (drop n xs) n

dropEvery2 :: [a] -> Int -> [a]
dropEvery2 = flip $ \n -> map snd . filter ((n/=) . fst) . zip (cycle [1..n])

dropEvery3 :: [a] -> Int -> [a]
dropEvery3 xs n = [i | (i,c) <- (zip xs [1,2..]), (mod c n) /= 0]

dropEvery4 :: [a] -> Int -> [a]
dropEvery4 lst n = snd $ foldl helper (1, []) lst
    where helper (i, acc) x = if n == i then (1, acc) else (i+1, acc ++ [x])
          
---------------------------------------------------------------------------

mySplit :: [a] -> Int -> ([a], [a])
mySplit xs n = (take n xs, drop n xs)

mySplit2 :: [a] -> Int -> ([a], [a])
mySplit2 xs n = foldl helper ([],[]) xs
                 where helper (f, s) x 
                         | length f < n = (f ++ [x], s)
                         | otherwise    = (f, s ++ [x])

mySplit3 :: [a] -> Int -> ([a], [a])
mySplit3 xs 0 = ([], xs)
mySplit3 (x:xs) n = let (f,l) = mySplit3 xs (n-1) 
                     in (x:f,l)

---------------------------------------------------------------

mySlice :: [a] -> Int -> Int -> [a]
mySlice xs low up 
    | up > low  = []
    | otherwise =  take (up-low+1) (drop (low-1) xs)


-- won't work with infinite list
mySlice2 :: [a] -> Int -> Int -> [a]
mySlice2 xs low up = snd $ foldl helper (1,[]) xs
                            where helper (i,ys) x 
                                   | i >= low && i <= up = (i+1,ys ++ [x])
                                   | otherwise           = (i+1,ys)
                                     
mySlice3 :: [a] -> Int -> Int -> [a]
mySlice3 (x:xs) i k
   | i > 1      = mySlice3 xs (i-1) (k-1)
   | k < 1      = []
   | otherwise  = x : mySlice3 xs (i-1) (k-1)
                  
-- won't work with infinite list 
mySlice4 xs i j = map snd
                  $ filter (\(x,_) -> x >= i && x <= j)
                  $ zip [1..] xs

-- list comprehension
mySlice5 xs i k = [x | (x,j) <- zip xs [1..k], i <= j]

-----------------------------------------------------------------------------

myRotate :: [a] -> Int -> [a]
myRotate [] _ = []
myRotate xs n = let ln = length xs
                 in let num = mod n ln
                     in (drop num xs) ++ (take num xs)

myRotate2 :: [a] -> Int -> [a]
myRotate2 xs n = take len . drop (n `mod` len) . cycle $ xs
        where len = length xs

myRotate3 :: [a] -> Int -> [a]
myRotate3 xs n
      | n < 0     = myRotate3 xs (n+len)
      | n > len   = myRotate3 xs (n-len)
      | otherwise = let (f, s) = splitAt n xs in s ++ f
     where len = length xs
           
myRotate4 :: [a] -> Int -> [a]
myRotate4 [] _ = []
myRotate4 x  0 = x
myRotate4 x  y
    | y > 0     = myRotate4 (tail x ++ [head x]) (y-1)
    | otherwise = myRotate4 (last x : init x) (y+1)

------------------------------------------------------------------

myRemoveAt :: [a] -> Int -> (a, [a])
myRemoveAt xs n 
  | n >= length xs = error "index too large"
  | otherwise      = ((!!) xs n, take n xs ++ drop (n+1) xs)
                     
myRemoveAt2 :: [a] -> Int -> (Maybe a, [a])
myRemoveAt2 []     _  = (Nothing, [])
myRemoveAt2 (x:xs) 0  = (Just x, xs)
myRemoveAt2 (x:xs) k  = let (a, r) = myRemoveAt2 xs (k-1) 
                         in (a, x:r)