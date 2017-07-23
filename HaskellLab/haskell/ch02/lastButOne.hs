-- various implementation to find the last but one element of a given list
lastButOne :: [a] -> a
lastButOne xs = if length (xs) <= 1
                then error "list too short"
                else if length (xs) == 2
                then head (xs)
                else lastButOne (tail xs)

lastButOne' :: [a] -> a
lastButOne' (x:xs) = if length xs == 1
                    then x
                    else lastButOne' xs

lastButOne'' :: [a] -> a
lastButOne'' (x1:[x2]) = x1
lastButOne'' (x:xs) = lastButOne'' xs

