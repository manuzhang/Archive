{-#LANGUAGE NoMonomorphismRestriction #-}
import System.Random
import Control.Monad(replicateM)
import Data.List
import Data.Ord(comparing)
import Data.Function(on)

myInsertAt :: a -> [a] -> Int -> [a]
myInsertAt x xs n = take n xs ++ [x] ++ drop n xs

myInsertAt2 :: a -> [a] -> Int -> [a]
myInsertAt2 x ys     0 = x:ys
myInsertAt2 x (y:ys) n = y : myInsertAt2 x ys (n-1)

myInsertAt3 :: a -> [a] -> Int -> [a]
myInsertAt3 el lst n = fst $ foldl helper ([], 0) lst
       where helper (acc, i) x = if i == n 
                                 then (acc ++ [el, x], i+1)                                                      else (acc ++ [x], i+1)

-------------------------------------------------------------------
range :: Int -> Int -> [Int]
range n1 n2 = [n1..n2]

range2 = enumFromTo

range3 :: Int -> Int -> [Int]
range3 n m 
   | n == m = [n]
   | n < m  = n : (range3 (n+1) m)
   | n > m  = n : (range3 (n-1) m)

---------------------------------------------------------------
-- extract a given number of randomly selected elements from a list
rnd_select :: [a] -> Int -> IO [a]
rnd_select xs n 
     | n < 0     = error "n >= 0"
     | otherwise = do pos <- replicateM n $ getStdRandom $ randomR (0, (length xs)-1)
                      return (map ((!!) xs) pos)

rnd_select2 :: [a] -> Int -> IO [a]
rnd_select2 [] _ = return []
rnd_select2 l  n 
      | n < 0     = error "n >= 0"
      | otherwise = do pos <- replicateM n $ getStdRandom $ randomR (0, (length l)-1)
                       return [l!!p | p <- pos]

rnd_select3 xs n 
     | n < 0     = error "n >= 0"
     | otherwise = replicateM n rand
         where rand = do r <- randomRIO (0, (length xs)-1)
                         return (xs!!r)

-- with no repetitions
rnd_selectIO :: [a] -> Int -> IO [a]
rnd_selectIO xs count = getStdRandom $ rnd_select4 xs count

rnd_select4 :: RandomGen g => [a] -> Int -> g -> ([a], g)                   
rnd_select4 _  0 gen  = ([], gen)
rnd_select4 [] _ gen  = ([], gen)
rnd_select4 xs count gen
  | count == (length xs) = (xs, gen)
  | otherwise            = rnd_select4 (snd $ myRemoveAt xs k) count gen'
                           where (k, gen') =
                                   randomR (0, (length xs) - 1) gen

myRemoveAt :: [a] -> Int -> (a, [a])
myRemoveAt xs n 
  | n >= length xs = error "index too large"
  | otherwise      = ((!!) xs n, take n xs ++ drop (n+1) xs)


-- the order is always preserved
rnd_select5 :: [a] -> Int -> IO [a]
rnd_select5 _      0 = return []
rnd_select5 (x:xs) n = 
    do r <- randomRIO (0, (length xs))
       if r < n 
          then do
              rest <- rnd_select5 xs (n-1)
              return (x : rest)
          else rnd_select5 xs n              
               
rnd_select6 :: [a] -> Int -> IO [a]
rnd_select6 _  0 = return []
rnd_select6 [] _ = return []
rnd_select6 xs count = do r <- randomRIO (0, (length xs)-1)
                          rest <- rnd_select6 (snd $ myRemoveAt xs r) (count-1)
                          return ((xs!!r) : rest)
                          
------------------------------------------------------------------------------------
diff_select :: Int -> Int -> IO [Int]
diff_select n m = rnd_select6 [1..m] n

-----------------------------------------------------------------------------------
rnd_permu :: [a] -> IO [a]
rnd_permu xs = rnd_select6 xs (length xs)

-- not good for long lists
rndElem :: [a] -> IO a
rndElem xs = do
  index <- randomRIO (0, (length xs)-1)
  return $ xs !! index
  
rndPermutation :: [a] -> IO [a]
rndPermutation xs = rndElem . permutations $ xs
  
--------------------------------------------------------------------------
combinations :: Int -> [a] -> [[a]]
combinations n xs 
    | n > length xs = combinations (length xs) xs
    | otherwise     = filter (\x -> (length x) == n) $ subsequences xs

combinations2 :: Int -> [a] -> [[a]]
combinations2 0 _  = [[]]
combinations2 n xs = [y:ys | y:xs' <- tails xs
                           , ys <- combinations2 (n-1) xs']           
                     
-- list monads here 
combinations3 :: Int -> [a] -> [[a]]
combinations3 0 _  = return []
combinations3 n xs = do y:xs' <- tails xs
                        ys <- combinations3 (n-1) xs'
                        return (y:ys)
                        
combinations4 :: Int -> [a] -> [[a]]
combinations4 0 _      = [[]]
combinations4 _ []     = [[]]
combinations4 n (x:xs) = (map (x:) (combinations (n-1) xs)) ++ (combinations n xs)

--------------------------------------------------------------------------------

-- this doesn't meet that "no permutations of the group members"
ggroup :: [Int] -> [[a]] -> [[[[a]]]]
ggroup [] _ = []
ggroup ns xs = map (localgroup ns) $  permutations xs
            where localgroup :: [Int] -> [[a]] -> [[[a]]]
                  localgroup []     _ = []
                  localgroup (i:is) ys = f : localgroup is s
                      where (f, s) = splitAt i ys                           
                  
-- this settles it
ggroup2 :: (Eq a) => [Int] -> [[a]] -> [[[[a]]]]
ggroup2 [] _ = []
ggroup2 (i:is) xs = filter (/= []) $ concatGroups (combinations i xs) (ggroup2 is xs)

concatGroups :: (Eq a) => [[[a]]] -> [[[[a]]]] -> [[[[a]]]]
concatGroups (x:xs) [] = [x] : (concatGroups xs [])
concatGroups [] ys     = ys
concatGroups xs (y:ys) = (foldl f [] xs) ++ if ys /= [] then concatGroups xs ys else []
                where f acc x = (if all (notOverlapped x) y then x:y else []) : acc

notOverlapped :: (Eq a) => [[a]] -> [[a]] -> Bool
notOverlapped _  []     = True
notOverlapped xs (y:ys) = (foldl f True xs) && notOverlapped xs ys
          where f acc x = acc && x /= y


-----------------------------------

combination :: Int -> [a] -> [([a],[a])]
combination 0 xs     = [([],xs)]
combination n []     = []
combination n (x:xs) = ts ++ ds
  where
    ts = [ (x:ys,zs) | (ys,zs) <- combination (n-1) xs ]
    ds = [ (ys,x:zs) | (ys,zs) <- combination  n    xs ]
 
group2 :: [Int] -> [a] -> [[[a]]]
group2 [] _ = [[]]
group2 (n:ns) xs =
    [ g:gs | (g,rs) <- combination n xs
           ,  gs    <- group2 ns rs ]
------------------------------------------------------------------------------    
-- sort by length of sublists

lsort :: [[a]] -> [[a]]    
lsort xs = sortBy myCompare xs

myCompare :: [a] -> [a] -> Ordering    
myCompare x y 
  | (length x) <  (length y) = LT
  | (length x)  > (length y) = GT
  | (length x) == (length y) = EQ


lsort2 :: [[a]] -> [[a]]
lsort2 = sortBy (comparing length)

lsort3 :: [[a]] -> [[a]]
lsort3 = sortBy (compare `on` length)

myCompare2 :: ([a], Int) -> ([a], Int) -> Ordering
myCompare2 x y
   | snd x <  snd y = LT
   | snd x  > snd y = GT
   | snd x == snd y = EQ

-- sort by length frequency of sublists
lfsort :: [[a]] -> [[a]]
lfsort xs = map fst $ sortBy myCompare2 $ addlength xs
  where addlength :: [[a]] -> [([a], Int)]
        addlength []     = []
        addlength (y:ys) = map (\w -> (w, length eq)) eq ++ addlength rest
            where (eq, rest)  = span (\x -> length x == length y) (lsort (y:ys))
                  
lfsort2 :: [[a]] -> [[a]]
lfsort2 lists = concat groups
    where groups = lsort $ groupBy (\x y -> length x == length y) $ lsort lists

lfsort3 :: [[a]] -> [[a]]
lfsort3 list = sortBy (\xs ys -> compare (frequency (length xs) list) (frequency (length ys) list)) list
             where frequency len l = length (filter (\x -> length x == len) l)