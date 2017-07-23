import Data.List(permutations)

-- Eight queens problem
-- length (queens 8) == 92

queens :: Int -> [[Int]]
queens n =  filter test ps
      where ps     = permutations [1..n]
            test p = and $ map aux p'
                where p'  = zip [1..n] p
                      aux (x,y) = and $ map (noDiag (x,y)) p' 
		      noDiag (x1,y1) (x2,y2) 
		           | x1 == x2  = True
               	           | otherwise = abs (x1-x2) /= abs (y1-y2)
