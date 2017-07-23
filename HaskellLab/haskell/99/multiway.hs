data Tree a = Node a [Tree a]
        deriving (Eq, Show)

nnodes :: Tree a -> Int
nnodes (Node _ ts) = (+1) $ sum $ map nnodes ts

-- afg^^c^bd^e^^^ <=> Node 'a' [Node 'f' [Node 'g' []],
--                              Node 'c' [],
--                              Node 'b' [Node 'd' [], Node 'e' []]]

stringToTree :: String -> Tree Char
stringToTree (x:xs@(y:ys)) 
     | y == '^'  = Node x []
     | otherwise = Node x (map stringToTree subs)
               where subs = snd $ foldl parse ([],[]) $ init xs
	             parse ([],[])      z = ([z], [[z]])
	             parse (stack, acc) z = (stack', acc')
			       where stack' 
			               | z == '^'  = init stack
				       | otherwise = stack ++ [z]
			             acc'   = if stack == []
				                then acc ++ [[z]]
						else (init acc) ++ [(last acc) ++ [z]] 

treeToString :: Tree Char -> String
treeToString (Node x ts) 
	  = [x] ++ concatMap treeToString ts ++ "^"

-- internal path length 
ipl2 :: Tree a -> Int 
ipl2 = ipl2' 0
    where ipl2' d (Node _ ts) = d + sum (map (ipl2' (d+1)) ts)

bottom_up :: Tree Char -> String
bottom_up (Node x ts) = concatMap bottom_up ts ++ [x]

-- a more efficient version using an accumlator
bottom_up2 :: Tree a -> [a]
bottom_up2 t = bottom_up_aux t []
    where bottom_up_aux :: Tree a -> [a] -> [a]
          bottom_up_aux (Node x ts) xs = foldr bottom_up_aux (x:xs) ts



-- "(a (f g) c (b d e))" <=> Node 'a' [Node 'f' [Node 'g' []],Node 'c' [],Node 'b' [Node 'd' [],Node 'e' []]]

display_lisp :: Tree Char -> String
display_lisp (Node x []) = [x]
display_lisp (Node x ts) = "(" ++ [x] ++ concatMap display_lisp ts ++ ")"

from_lisp :: String -> Tree Char
from_lisp [x]   = Node x []
from_lisp lisps = Node x (map from_lisp subs) 
      where (x:xs) = init $ tail $ lisps
            subs = snd $ foldl parse ([],[]) xs
	    parse ([],[])      z 
	       | z == '('  = ([z], [[z]])
	       | z == ' '  = ([], [])
	       | otherwise = ([], [[z]])
	    parse (stack, acc) z = (stack', acc')
	        where stack'
		         | z == '('  = stack ++ [z]
			 | z == ')'  = init stack
			 | otherwise = stack
		      acc' 
		         | z /= ' '  = if stack == []
		                        then acc ++ [[z]]
				        else init acc ++ [(last acc ++ [z])]
                         | otherwise = acc

addspace :: String -> String
addspace [x]                 = [x] 
addspace (x:y:xs)
      | x /= '(' && y /= ')' = x:' ':addspace (y:xs)
      | otherwise            = x:addspace (y:xs)
