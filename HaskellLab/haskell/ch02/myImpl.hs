-- implementation of library functions 
-- illustration of pattern matching

and2 True True  = True
and2 _    _     = False
 
not2 True  = False
not2 False = True 

or2 False False = False
or2 _     _     = True 

myDrop :: Int -> ([a] -> [a])
myDrop n xs = if n <= 0 || null xs
              then xs
	      else myDrop (n - 1) (tail xs)

niceDrop n xs | n <= 0 = xs
niceDrop _ []          = []
niceDrop n (_:xs)      = niceDrop (n - 1) xs
