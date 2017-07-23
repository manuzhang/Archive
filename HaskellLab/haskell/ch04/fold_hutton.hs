sumlength     :: [Int] -> (Int, Int)
sumlength xs  =  foldr (\n (x,y) -> (n + x, 1 + y)) (0,0) xs

sumlength2        :: [Int] -> (Int, Int)
sumlength2   []   = (0,0)
sumlength2 (x:xs) = (\n (a,b) -> (n + a, 1 + b)) x (sumlength2 xs)


dropWhile' p ys = foldr f v ys
           where f x (ys, xs) = (if p x then ys else x : xs, x : xs)
                 v            = ([], [])
myDropWhile p = fst.dropWhile' p

dropWhile2           :: (a -> Bool) -> ([a] -> [a])
dropWhile2 p  []     =  []
dropWhile2 p (x:xs)  =  if p x 
                        then dropWhile2 p xs 
                        else x:xs
                        
-- writing foldl using foldr
-- refactoring foldl 
foldl         :: (a -> b -> a) -> a -> [b] -> a
foldl f v xs  = g xs v
        where g   []   v = v
              g (x:xs) v = g xs (f v x)
              
foldl f v xs = g xs v
        where g   []     = \v -> v
              g (x:xs) v = g xs (f v x)

foldl f v xs = g xs v 
        where g   []     = id
              g (x:xs) v = g xs (f v x)
-- universal property of fold
g = foldr k w
-- if g is going to written in foldr it has to satisfy the following definition
g   []   = w 
g (x:xs) = k x (g xs)

-- from the definition of g we can infer that w is id
-- but we still need to calculate the definition of k
g (x:xs)     = k x (g xs)
g (x:xs) v   = k x (g xs) v      -- accumlator of functions
g xs (f v x) = k x (g xs) v      -- definition of foldl
g' (f v x)   = k x g' v          -- generalize (g xs) to g'
k = \x g' -> (\v -> g' (f v x))

foldl f v xs = foldr k w xs v
          where k x g' v = g' (f v x)
                w        = id
                



