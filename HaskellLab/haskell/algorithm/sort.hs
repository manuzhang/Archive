-- all the results in increasing order
-- TEST:
--      merge_sort $ take 10000 $ randomRs (1,10000) $ mkStdGen 1 ::[Int]
 
import Data.List(minimum, partition)
import Data.Array

-- this is an ugly, naive solution
-- translated from peusdocode literally
quick_sort :: (Ord a, Show a) => [a] -> [a]
quick_sort [] = []
quick_sort keys = aux keys 0 (length keys - 1) 
       where aux xs p q 
                   | p < q     = aux less 0 (r-1) ++ pivot ++ aux more 0 (q-r-1)
		   | otherwise = xs 
                where pivot    = [xs'!!r]
		      less     = take r xs'
		      more     = drop (r+1) xs'
		      (xs', r) = par xs
             par xs = trav xs 1 (length xs - 1) 0
	        where trav ys i j k 
		            | i <= j    = if (ys!!i) <= (xs!!0) 
		                           then trav (exch ys (k+1) i) (i+1) j (k+1)
		       		           else trav ys (i+1) j k
		            | otherwise = (exch ys 0 k, k) 
	     exch xs i j 
	           | i /= j    = (take i xs) ++ [xs!!j] ++ (take (j-i-1) $ drop (i+1) xs) 
	                         ++ [xs!!i] ++ (drop (j+1) xs)
                   | otherwise = xs
	
-- list comprehension
-- it sacrifies some performance in order to achieve its remarkable clarity
-- note it parses the list twice to get lesser parts and greater parts
qsortOneLine :: (Show a, Ord a) => [a] -> [a]
qsortOneLine [] = []
qsortOneLine (x:xs) = qsortOneLine [y | y <- xs, y < x] 
	              ++ x : qsortOneLine [y | y <- xs, y >= x]

-- let the List modules's partition function do the dirty work
quick_sort2 :: (Show a, Ord a) => [a] -> [a]
quick_sort2 []     = []
quick_sort2 (x:xs) = quick_sort2 leq ++ [x] ++ quick_sort2 gtr
             where (leq, gtr) = partition (<= x) xs 

insertion_sort :: (Ord a, Show a) => [a] -> [a]
insertion_sort []   = []
insertion_sort keys = foldr f [] keys
               where f key []        = [key]
                     f key acc       = insert key acc
                     insert y []     = [y] 
                     insert y (x:xs) 
                         | x < y     = x : insert y xs
                         | otherwise = y : x : xs

insertion_sort2 :: (Ord a, Show a) => [a] -> [a]
insertion_sort2 []     = []
insertion_sort2 (x:[]) = [x]
insertion_sort2 keys   = insert (last keys) $ insertion_sort2 (init keys) 
                   where insert y []     = [y]
                         insert y xs  
                          | y < last xs  = insert y (init xs) ++ [last xs]
                          | otherwise    = xs ++ [y]

merge_sort :: (Ord a, Show a) => [a] -> [a]
merge_sort []     = []
merge_sort (x:[]) = [x]
merge_sort keys   = merge  (merge_sort (take len keys)) (merge_sort (drop len keys))
          where len         = length keys `div` 2 
                merge :: (Ord a) => [a] -> [a] -> [a]
                merge (x:xs) []     = (x:xs)
                merge []     (y:ys) = (y:ys)
                merge (x:xs) (y:ys) = if x <= y 
                                      then x : merge (xs) (y:ys)
                                      else y : merge (x:xs) ys

selection_sort :: (Ord a, Show a) => [a] -> [a]
selection_sort []     = []
selection_sort (x:[]) = [x] 
selection_sort keys   = s : if (s == h)
                            then selection_sort $ tail keys
                            else selection_sort $ tail bef ++ [h] ++ tail aft 
                             where (bef,aft) = span (/=s) keys 
                                   s = minimum keys
                                   h = head keys

bubble_sort :: (Ord a, Show a) => [a] -> [a]
bubble_sort []     = []
bubble_sort (x:[]) = [x]
bubble_sort keys   = b : bubble_sort bs
               where (b:bs) = foldr bubble [] keys
                     bubble x []      = [x]
                     bubble x (y:ys) 
                          | x > y     = y : x : ys
                          | otherwise = x : y : ys 

listToArray :: (Ord a) => [a] -> Array Int a
listToArray xs = listArray (0, len-1) xs
         where len = length xs 

binary_search :: (Ord a) => a -> Array Int a -> Int
binary_search x array = recur_search x ys f l
              where (f,l) = bounds array
                    ys  = elems array
                    recur_search :: (Ord a) => a -> [a] -> Int -> Int -> Int
                    recur_search x' ys' f' l' 
                                 | f' > l'    = -1
                                 | otherwise  = if x' == mv
                                                then m
                                                else if x' < mv
                                                     then recur_search x' ys' f' (m-1)
                                                     else recur_search x' ys' (m+1) l'
                                 where m = (l' - f') `div` 2 + f'
                                       mv = (!!) ys' m

-- determines the number of inversions in any permutation on N elements in theta(NlgN) worst-case time
inversion_num :: (Ord a) => [a] -> Int
inversion_num []   = 0
inversion_num keys = getNum $ divide $ addcount keys
          where addcount  = map (\x -> (x,0))
                getNum    = sum . map (\(x,n) -> n) 
                merge :: (Ord a) => [(a,Int)] -> [(a,Int)] -> [(a,Int)]
                merge (x:xs)     []         = (x:xs)
                merge []         (y:ys)     = (y:ys)
                merge ((x,m):xs) ((y,n):ys) 
                     | x > y     = (y,n) : merge (map (\(x,m) -> (x,m+1)) ((x,m):xs)) ys
                     | otherwise = (x,m) : merge xs ((y,n):ys)
                divide :: (Ord a) => [(a,Int)] -> [(a,Int)]
                divide (x:[]) = [x]
                divide ks = merge (divide fhalf) (divide shalf)
                         where len = length ks `div` 2
                               fhalf = take len ks
                               shalf = drop len ks
                
