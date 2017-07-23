import Data.Maybe (fromMaybe)
import Data.Char (digitToInt)
import Data.Char (isDigit)
import Data.Char (isSpace)
import Data.List (foldl')

-- P84 E1
safeHead :: [a] -> Maybe a 
safeHead (x:_) = Just x
safeHead []    = Nothing

safeTail :: [a] -> Maybe [a]
safeTail (_:xs) = Just xs
safeTail []     = Nothing

safeLast :: [a] -> Maybe a
safeLast (x:xs) = if null xs 
                  then Just x
                  else safeLast xs
safeLast []     = Nothing

safeInit :: [a] -> Maybe [a]
safeInit (x:xs) = if null xs 
                  then Nothing
                  else Just (x:(fromMaybe [] (safeInit xs)))
safeInit []     = Nothing

-- this may not be correct
-- <a href="typedef.me/?page=3"/>
safeInit2 :: [a] -> Maybe [a]
safeInit2 []     = Nothing
safeInit2 [x]    = Just []
safeInit2 (x:xs) = Just (x:fromMaybe xs (safeInit xs)) 

-- P84 E2
splitWith :: (a -> Bool) -> [a] -> [[a]]
splitWith p xs 
       | null xs   = []
       | otherwise = if p (head xs)
                     then ((take 1 xs):[])
                           ++ splitWith p (drop 1 xs)
                     else splitWith p (drop 1 xs)

-- P84 E3
-- @firstWord.hs

-- P84 E4
-- @convert.hs

-- P97.1
asInt_fold :: String -> Int
asInt_fold ('-':xs) = - asInt_fold xs
asInt_fold xs = foldl' step 0 xs
       where step acc x
              | acc < 0   = error "Int overflow"
              | otherwise = 10 * acc + digitToInt x
              
-- P97.4
type ErrorMessage = String
asInt_either :: String -> Either String Int
asInt_either ('-':xs) = let ans = asInt_either xs
                        in case ans of 
                             Left  l -> Left l
                             Right r -> Right (-r)
asInt_either xs = foldl' step (Right 0) xs
       where step (Right acc) x
              | acc < 0         = Left "Int overflow"
              | not (isDigit x) = Left ("not digit" ++ ":" ++ (replicate 1 x))
              | otherwise       = Right (10 * acc + digitToInt x)
             step (Left l) _    = Left l
             
-- P97.5
myConcat :: [[a]] -> [a]
myConcat xss = foldr (++) [] xss

-- P97.7
myTakeWhile :: (a -> Bool) -> [a] -> [a]
myTakeWhile _ []       = []
myTakeWhile p (x:xs) 
         | p x         = x : myTakeWhile p xs
         | otherwise   = []
         
myTakeWhile2 :: (a -> Bool) -> [a] -> [a]
myTakeWhile2 p xs 
         | null xs     = []   
         | otherwise   = foldr step [] xs
                               where step x ys 
                                      | p x       = x : ys
                                      | otherwise = []
                                      
-- P97.8                                    
myGroupBy :: (a -> a -> Bool) -> [a] -> [[a]]
myGroupBy p   []    = []
myGroupBy p (x:xs)  = [x:(take n xs)] ++ myGroupBy p (drop n xs)
                                 where n = if null xs 
                                           then 0
                                           else if p x (head xs)
                                                then 1
                                                else 0

            
myGroupBy'      :: (a -> a -> Bool) -> [a] -> ([[a]], [a])                        
myGroupBy' p xs = foldl f ([], []) xs
              where f ([], []) x          = ([], [x])
                    f (a1, a2) x 
                          | null a2       = (a1 ++ [[x]], [x])
                          | p (last a2) x = ((if (length a1 <= 1) then a1 else init a1) ++ [[(last a2), x]], [])
                          | otherwise     = ((if (length a1 <= 1) then a1 else init a1) ++ [[(last a2)], [x]], [x])
                                                                        
myGroupBy2      :: (a -> a -> Bool) -> [a] -> [[a]]
myGroupBy2 p xs = fst (myGroupBy' p xs)

myGroupBy3        :: (a -> a -> Bool) -> [a] -> [[a]]
myGroupBy3 cmp xs = foldl' f [] xs
     where f []  x = [[x]]
           f acc x | cmp (head (last acc)) x = (init acc) ++ [(last acc) ++ [x]]
                   | otherwise               = acc ++ [[x]]

myAny       :: (a -> Bool) -> [a] -> Bool
myAny  p xs = foldr (\x y -> (p x) || y) False xs

                 
                                
myWords'     :: String -> ([String], Bool)
myWords' cs  =  foldl f ([], True) cs
           where  f ([], _)   c                 
                    | isSpace c                  =  ([], True)
                    | otherwise                  =  ([[c]], False)
                  f (acc, sp) c 
                    | isSpace c                  =  (acc, True)   
                    | sp                         =  (acc ++ [[c]], False)
                    | otherwise                  =  ((init acc) ++ [last acc ++ [c]], False)
                    
myUnlines       :: [String] -> String
myUnlines  css  =  foldr (\x y -> x ++ "\n" ++ y) "" css
myWords      :: String -> [String]
myWords  cs  = fst (myWords' cs)

myCycle        :: [a] -> [a]
myCycle (x:xs) = x:(myCycle (xs ++ [x]))

myCycle'       :: [a] -> [a]
myCycle' []    = []
myCycle' xs    = xs'
               where xs' = foldr (:) xs' xs  
               
ack               :: [Int] -> [Int] -> [Int]
ack   []    ys     = 1:ys
ack (x:xs)  []     = ack xs [1]
ack (x:xs) (y:ys)  = ack xs (ack (x:xs) ys)
