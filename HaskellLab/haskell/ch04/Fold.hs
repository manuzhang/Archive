foldlSum xs = foldl step 0 xs
    where step acc x = acc + x
    
myFilter p xs = foldr step [] xs
         where step x ys | p x        = x:ys
                         | otherwise  = ys
{-- 
myFilter even [1..10] == foldr step [] [1..10]
                      == 1 `step` (2 `step` ... (10 `step` [])...)
--}                 
                                      

myFilter2 p xs = foldl step [] xs
         where step ys x | p x       = x:ys
                         | otherwise = ys


{--
myFilter2 even [1..10] == foldl step [] [1..10]
                       == (...(([] `step` 1) `step` 2) `step` ...) `step` 10
--}

myFoldl :: (a -> b -> a) -> a -> [b] -> a
myFoldl f z xs = foldr step id xs z 
                where step x g a = g (f a x)
{--
 myFoldl (+) 0 [1, 2, 3]
    = (foldR step id [1, 2, 3]) 0
    = (step 1 (step 2 (step 3 id))) 0
    = (step 1 (step 2 (\a3 -> id ((+) a3 3)))) 0
    = (step 1 (\a2 -> (\a3 -> id ((+) a3 3)) ((+) a2 2))) 0
    = (\a1 -> (\a2 -> (\a3 -> id ((+) a3 3)) ((+) a2 2)) ((+) a1 1)) 0
    = (\a1 -> (\a2 -> (\a3 -> (+) a3 3) ((+) a2 2)) ((+) a1 1)) 0
    = (\a1 -> (\a2 -> (+) ((+) a2 2) 3) ((+) a1 1)) 0
    = (\a1 -> (+) ((+) ((+) a1 1) 2) 3) 0
    = (+) ((+) ((+) 0 1) 2) 3
    = ((0 + 1) + 2) + 3
--}

foldl' _  zero  [] = zero
foldl' step zero (x:xs) =
        let new = step zero x
        in new `seq` foldl' step new xs
