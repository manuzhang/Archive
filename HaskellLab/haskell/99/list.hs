import Data.List(group, findIndex)
import Control.Arrow

myLast :: [a] -> Maybe a
myLast []     = Nothing
myLast (x:xs) = if null xs  
                then Just x
                else myLast xs                         
                     
myLast2 :: [a] -> Maybe a 
myLast2 []  = Nothing
myLast2 [x] = Just x
myLast2 (_:xs) = myLast2 xs

myLast3 = foldr1 (const id)
myLast4 = foldr1 (flip const)
myLast5 = head . reverse

myButLast :: [a] -> Maybe a
myButLast []       = Nothing
myButLast (x:xs) 
  | length xs == 0 = Nothing
  | length xs == 1 = Just x
  | otherwise      = myButLast xs

myButLast2 :: [a] -> a
myButLast2 = last . init

myButLast3 x = reverse x !! 1

myButLast4 [x,_]  = x
myButLast4 (_:xs) = myButLast4 xs

myButLast5 = head . tail . reverse

-----------------------------------------------

-- elementAt [1..] 0 will hang the process 
elementAt :: [a] -> Int -> a
elementAt (x:_)  1  = x
elementAt (_:xs) n  = elementAt xs (n - 1)
elementAt _      _  = error "Index out of bounds"

elementAt2 :: [a] -> Int -> a
elementAt2 (x:_) 1  = x
elementAt2 [] _     = error "Index out of bounds"
elementAt2 (_:xs) n 
  | n < 1     = error "Index out of bounds"
  | otherwise = elementAt2 xs (n-1)
                        
elementAt3 xs n = last . take n $ xs
elementAt4 xs n = head . reverse . take n $ xs

----------------------------------------------

myLength :: [a] -> Int
myLength []     = 0
myLength (_:xs) = 1 + myLength xs
                
myLength1 = foldl (\n _ -> n + 1) 0                  
myLength2 = foldr (\_ n -> n + 1) 0
myLength3 = foldr (\_ -> (+1)) 0
myLength4 = foldr ((+) . (const 1)) 0
myLength5 = foldr (const (+1)) 0
myLength6 xs = snd $ last $ zip xs [1..]
myLength7 = fst . last . (zip [1..])

--------------------------------------------------

myReverse :: [a] -> [a]
myReverse []     = []
myReverse (x:xs) = reverse xs ++ [x]

myReverse2 :: [a] -> [a]
myReverse2 = foldl (flip (:)) []

myReverse3 = foldr (\x ys-> ys ++ [x]) []

myReverse4 list = reverse' list []
   where
     reverse' []     reversed = reversed
     reverse' (x:xs) reversed = reverse' xs (x:reversed)

isPalindrome :: (Eq a) => [a] -> Bool
isPalindrome []          = False
isPalindrome (x:xs) 
  | myLength (x:xs) == 1 = True
  | x /= last xs         = False    
  | otherwise            = isPalindrome (init xs)

isPalindrome2 :: (Eq a) => [a] -> Bool
isPalindrome2 xs = xs == (reverse xs)

isPalindrome3 []  = True
isPalindrome3 [_] = True
isPalindrome3 xs  = (head xs) == (last xs) && (isPalindrome3 $ init $ tail xs)

isPalindrome4 :: (Eq a) => [a] -> Bool
isPalindrome4 xs = foldl (\acc (a,b) -> if a == b then acc else False) True input
            where input = zip xs (reverse xs)

-- does half as many compares
isPalindrome5 :: (Eq a) => [a] -> Bool
isPalindrome5 xs = p [] xs xs
   where p rev (x:xs) (_:_:ys) = p (x:rev) xs ys
         p rev (x:xs) [_]      = rev == xs
         p rev xs     []       = rev == xs

isPalindrome6 :: (Eq a) => [a] -> Bool
isPalindrome6 xs = foldr (&&) True $ zipWith (==) xs (reverse xs)

-----------------------------------------------------------------------
-- eliminate consecutive duplicates of list lements
myCompress :: (Eq a) => [a] -> [a]
myCompress []   = []
myCompress (x:xs) 
    | null xs   = [x]
    | otherwise = localCompress (xs ++ [x]) (myLength xs)
                  
localCompress :: (Eq a) => [a] -> Int -> [a]
localCompress []     _    = []  
localCompress (y:ys) n
           | n == 0       = (y:ys)
           | y == last ys = localCompress ys (n-1)
           | otherwise    = localCompress (ys ++ [y]) (n-1)
                            
myCompress2 :: (Eq a) => [a] -> [a]
myCompress2 = map head . group

myCompress3 :: (Eq a) => [a] -> [a]
myCompress3 (x:ys@(y:_))
          | x == y    = myCompress3 ys
          | otherwise = x:myCompress3 ys
myCompress3 x = x

myCompress4 :: (Eq a) => [a] -> [a]
myCompress4 = foldr skipDups []
     where skipDups x [] = [x]
           skipDups x acc
               | x == head acc = acc
               | otherwise     = x : acc
                                 
myCompress5 []     = []
myCompress5 (x:xs) = x : (myCompress5 $ dropWhile (== x) xs)

myCompress6 x = foldr (\a b -> if a == (head b) then b else a:b) [last x] x

------------------------------------------------------------------------
-- encode "aaaabccaadeeee"
-- [(4,'a'),(1,'b'),(2,'c'),(2,'a'),(1,'d'),(4,'e')]
myEncode :: (Eq a) => [a] -> [(Int, a)]
myEncode []     = []
myEncode (x:xs) = localEncode xs [(1, x)]
   
localEncode :: (Eq a) => [a] -> [(Int, a)] -> [(Int, a)]
localEncode [] ts        = ts 
localEncode (y:ys) ts 
           | y == t2     = localEncode ys (init ts ++ [(t1 + 1, t2)]) 
           | otherwise   = localEncode ys (ts ++ [(1, y)])
              where t1 = fst $ last ts
                    t2 = snd $ last ts
                    
myEncode2 xs = map (\x -> (length x, head x)) (group xs)

myEncode3 xs = map (length &&& head) $ group xs

---------------------------------------------------------------------
data NestedList a = List [NestedList a]
            | Elem a
            deriving (Show)
-- flatten (List [Elem 1, List [Elem 2, List [Elem 3, Elem 4], Elem 5]])
-- [1,2,3,4,5]
flatten :: NestedList a -> [a]
flatten (List [])       = []
flatten (Elem x)        = [x]
flatten (List xs)       = concatMap flatten xs
  
flatten2 :: NestedList a -> [a]
flatten2 (Elem a)      = [a]
flatten2 (List (x:xs)) = flatten x ++ flatten (List xs)
flatten2 (List [])     = []

flatten3 :: NestedList a -> [a]
flatten3 a = flt' a []
  where flt' (Elem x)      xs = x : xs
        flt' (List (x:ls)) xs = flt' x (flt' (List ls) xs)
        flt' (List [])     xs = xs

flatten4 :: NestedList a -> [a]
flatten4 (Elem x)  = [x]                               
flatten4 (List xs) = foldr (++) [] $ map flatten4 xs

-------------------------------------------------------------------
-- pack consecutive duplicates of list elements into sublists
pack :: (Eq a) => [a] -> [[a]]
pack [] = []
pack xs = foldr f [] xs
    where f x ys
           | ys == []              = [[x]]
           | x == (head $ head ys) = [[x] ++ (head ys)] ++ (myTail ys)
           | otherwise             = [[x]] ++ ys
                                     
myTail :: [[a]] -> [[a]]                          
myTail []     = []
myTail (x:xs) = xs

pack2 (x:xs) = let (first, rest) = span (==x) xs
                in (x:first) : pack rest
pack2 []     = []

pack3 []     = []
pack3 (x:xs) = (x:reps) : (pack rest)
     where
       (reps, rest) = maybe (xs, []) (\i -> splitAt i xs) (findIndex (/=x) xs)
       
pack4 [] = []
pack4 (x:xs) = (x : takeWhile (==x) xs) : pack4 (dropWhile (==x) xs)
