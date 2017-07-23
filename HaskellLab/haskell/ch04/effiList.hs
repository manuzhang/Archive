myDumbExample xs = if length xs > 0
                   then head xs 
                   else 'Z'
-- a list doesn't store its own length explicitly
-- length walks the entire list

mySmartExample xs = if not (null xs)
                    then head xs
                    else 'Z'
             
myOtherExample (x:_) = x
myOtherExample [] = 'Z'
