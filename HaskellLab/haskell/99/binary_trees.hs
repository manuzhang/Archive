import Data.List(findIndex, group)
import Data.Maybe(fromJust)
import Text.Parsec.String
import Text.Parsec hiding (Empty)

data Tree a = Empty | Branch a (Tree a) (Tree a)
              deriving (Show, Eq)
               
-- construct one balanced binary tree of N nodes
onebalTree :: Int -> Tree Char
onebalTree 0 = Empty
onebalTree n = Branch 'x' (onebalTree left) (onebalTree right)
                where right = (n-1) `div` 2
                      left  = (n-1) - right 

-- construct all balanced binary trees of N nodes
cbalTree :: Int -> [Tree Char]
cbalTree 0 = [Empty]
cbalTree n = [Branch 'x' lsubTree rsubTree
              | left <- [((n-1) `div` 2)..(n `div` 2)],
                lsubTree <- cbalTree left, 
                rsubTree <- cbalTree (n-1-left) 
              ] 
----------------------------------------------------------------
-- alternative solution
leaf x = Branch x Empty Empty
 
main = putStrLn $ concatMap (\t -> show t ++ "\n") balTrees
    where balTrees = filter isBalancedTree (makeTrees 'x' 4)
 
isBalancedTree :: Tree a -> Bool
isBlanacedTree Empty = True
isBalancedTree (Branch _ l r) = abs (countBranches l - countBranches r) <= 1
                                && isBalancedTree l && isBalancedTree r
isBalancedTree _ = False
 
countBranches :: Tree a -> Int
countBranches Empty = 0
countBranches (Branch _ l r) = 1 + countBranches l + countBranches r
 
-- makes all possible trees filled with the given number of nodes
-- and fill them with the given value
makeTrees :: a -> Int -> [Tree a]
makeTrees _ 0 = []
makeTrees c 1 = [leaf c]
makeTrees c n = landr
    where lonly  = [Branch c t Empty | t <- smallerTree]
          ronly = [Branch c Empty t | t <- smallerTree]
          landr = concat [[Branch c l r | l <- fst lrtrees, r <- snd lrtrees] | lrtrees <- treeMinusTwo]
          smallerTree = makeTrees c (n-1)
          treeMinusTwo = [(makeTrees c num, makeTrees c (n-1-num)) | num <- [0..n-2]]

---------------------------------------------------------------
-- symmetric binary trees
-- this requires all the elements to be the same 
mirror :: (Eq a) => Tree a -> Tree a
mirror Empty                        = Empty
mirror (Branch a lsubTree rsubTree) = Branch a (mirror rsubTree) (mirror lsubTree)

symmetric :: (Eq a) => Tree a -> Bool
symmetric Empty = True
symmetric t     = t == mirror t

-- so this is better
mirror2 :: Tree a -> Tree a -> Bool
mirror2 Empty            Empty            = True
mirror2 (Branch _ ll lr) (Branch _ rl rr) = (mirror2 ll rr) && (mirror2 lr rl)
mirror2 _                 _               = False

symmetric2 :: Tree a -> Bool
symmetric2 (Branch _ l r) = mirror2 l r

---------------------------------------------------------------
-- binary search trees
insert :: (Eq a, Ord a) => a -> Tree a -> Tree a
insert x Empty                        = Branch x Empty Empty
insert x (Branch y lsubTree rsubTree)  
         | x <= y                     = Branch y (insert x lsubTree) rsubTree
         | otherwise                  = Branch y lsubTree (insert x rsubTree)


construct :: (Eq a, Ord a) => [a] -> Tree a
construct xs = foldl (flip insert) Empty xs

--------------------------------------------------------------------
-- symmetric, completely balanced binary trees
symCbalTrees :: Int -> [Tree Char]
symCbalTrees = filter  symmetric2 . cbalTree 

-----------------------------------------------------------------
-- construct height balanced binary trees
hbalTrees2 :: a -> Int -> [Tree a]
hbalTrees2 x 0 = [Empty]
hbalTrees2 x 1 = [Branch x Empty Empty]
hbalTrees2 x h = [Branch x l r |
        (hl, hr) <- [(h-2, h-1), (h-1, h-1), (h-1, h-2)],
        l <- hbalTrees2 x hl, r <- hbalTrees2 x hr]

-- this fibonacci-like solution is just so brilliant
hbalTrees3 :: a -> Int -> [Tree a]
hbalTrees3 x h = trees !! h
  where trees = [Empty] : [Branch x Empty Empty] :
                zipWith combine (tail trees) trees
        combine ts shortts = [Branch x l r |
                (ls, rs) <- [(shortts, ts), (ts, ts), (ts, shortts)],
                l <- ls, r <- rs]

-----------------------------------------------------------------------
-- minimum number of nodes of height-balanced binary tree
minNodes :: Int -> Int
minNodes 0 = 0
minNodes 1 = 1
minNodes height = 1 + minNodes (height-1) + minNodes (height-2) 

-- max height with N nodes of height-balanced binary tree
maxHeight :: Int -> Int
maxHeight 0 = 0
maxHeight 1 = 1
maxHeight n = 1 + maximum [maxHeight ln | ln <- [0..n-1], abs (maxHeight ln - maxHeight (n-1-ln)) <= 1]

minHeight :: Int -> Int
minHeight n = fromIntegral $ ceiling $ logBase 2.0 (fromIntegral (n+1))

numOfNodes :: Tree a -> Int
numOfNodes Empty                        = 0
numOfNodes (Branch _ lsubTree rsubTree) = 1 + numOfNodes lsubTree + numOfNodes rsubTree

-- height balanced binary trees of N nodes
hbalTreeNodes :: a -> Int -> [Tree a]
hbalTreeNodes x n = filter (\t -> (numOfNodes t) == n) $ concatMap (hbalTrees2 x) [minHeight n..maxHeight n]
             
-- this is much more efficient
hbalTreeNodes2 _ 0 = [Empty]
hbalTreeNodes2 x n = concatMap toFilteredTrees [minHeight .. maxHeight]
    where toFilteredTrees = filter ((n ==) . countNodes) . hbalTrees2 x 
          -- Similar to the Fibonacci sequence but adds 1 in each step.
          minNodesSeq = 0:1:zipWith ((+).(1+)) minNodesSeq (tail minNodesSeq)
          minNodes = (minNodesSeq !!)
 
          minHeight = ceiling $ logBase 2 $ fromIntegral (n+1)
          maxHeight = (fromJust $ findIndex (>n) minNodesSeq) - 1
 
          countNodes Empty = 0
          countNodes (Branch _ l r) = countNodes l + countNodes r + 1 

---------------------------------------------------------------------------
-- leaves, internals and internal at level N
countLeaves :: Tree a -> Int
countLeaves Empty = 0
countLeaves (Branch _ Empty Empty) = 1
countLeaves (Branch _ lsubTree rsubTree) = countLeaves lsubTree + countLeaves rsubTree

leaves :: Tree a -> [a]
leaves Empty = []
leaves (Branch x Empty Empty) = [x]
leaves (Branch _ lsubTree rsubTree) = leaves lsubTree ++ leaves rsubTree

internals :: Tree a -> [a]
internals Empty = []
internals (Branch x Empty Empty) = []
internals (Branch x lsubTree rsubTree) = [x] ++ internals lsubTree ++ internals rsubTree

atLevel :: Tree a -> Int -> [a]
atLevel Empty _ = []
atLevel (Branch x lsubTree rsubTree) n 
       | n == 1    = [x] 
       | n > 1     = atLevel lsubTree (n-1) ++ atLevel rsubTree (n-1)
       | otherwise = []

-----------------------------------------------------------------------------
-- complete binary tree of N nodes
-- address of left child of node at id is 2 * id and that of right child is 2 * id + 1
completeBinaryTree :: Int -> Tree Char
completeBinaryTree n = buildTree 1
                 where buildTree :: Int -> Tree Char
                       buildTree id  
                          | id <= n   = Branch 'x' (buildTree (2*id)) (buildTree (2*id+1))
                          | otherwise  = Empty

isCompleteBinaryTree :: Tree Char -> Bool
isCompleteBinaryTree Empty = True
isCompleteBinaryTree t     = foldl parse True $ map (atLevel2 t) [1..getHeight t]  
                       where getHeight Empty          = 0
		             getHeight (Branch x l r) = 1 + max (getHeight l) (getHeight r)        
                             parse acc y              = acc && ((==[]) $ snd $ span (==' ') $ dropWhile (/=' ') y)

atLevel2 :: Tree Char -> Int -> [Char]
atLevel2 Empty _ = " "
atLevel2 (Branch x lsubTree rsubTree) n 
       | n == 1    = [x] 
       | n > 1     = atLevel2 lsubTree (n-1) ++ atLevel2 rsubTree (n-1)
       | otherwise = []


-- another solution to check complete binary tree
filled :: Tree a -> [[Bool]]                                                                                                                             
filled Empty = [[False]]
filled (Branch _ l r) = [True] : zipWith (++) (filled l) (filled r)                                                                                     

isCompleteBinaryTree2 :: Tree a -> Bool                                                                                                              
isCompleteBinaryTree2 Empty = True                                                                                                                
isCompleteBinaryTree2 t     = and $ last_proper : zipWith (==) lengths powers    
        where levels  = filled t     
	-- The upper levels of the tree should be filled.                                                                                                
        -- Every level has twice the number of nodes as the one above it,                                                                               
        -- so [1,2,4,8,16,...]                                                                                                                          
              lengths = map (length . filter id) $ init levels                                                                                  
              powers  = iterate (2*) 1                                                                                                          
        -- The last level should contain a number of filled spots,                                                                              
	-- and (maybe) some empty spots, but no filled spots after that!                          
              last_filled = map head $ group $ last levels                                        
	      last_proper = head last_filled && (length last_filled) < 3

-- another solution to construct complete binary tree
completeBinaryTree2 :: Int -> a -> Tree a
completeBinaryTree2 n = cbtFromList . replicate n

-- can't understand it
cbtFromList :: [a] -> Tree a
cbtFromList xs = let (t, xss) = cbt (xs:xss) in t
    where cbt ((x:xs):xss) =
                  let (l,xss')  = cbt xss
		      (r,xss'') = cbt xss'
		  in  (Branch x l r, xs:xss'')
          cbt _            = (Empty,[])

lookupIndex :: Tree a -> Integer -> a
lookupIndex t = lookup t . path
     where lookup Empty _ = error "index to large"
           lookup (Branch x _ _) [] = x
	   lookup (Branch x l r) (p:ps) = lookup (if even p then l else r) ps
	   path = reverse . takeWhile (>1) . iterate (`div` 2) . (1+)

completeHeight Empty = Just 0
completeHeight (Branch _ l r) = do
          hr <- completeHeight r
	  hl <- completeHeight l
	  if (hl == hr) || (hl - hr == 1)
	    then return $ 1+hl
	    else Nothing

-----------------------------------------------------------------------------
-- tree64 = (Branch 'n' (Branch 'k' (Branch 'c' (Branch 'a' Empty Empty)(Branch 'h' (Branch 'g' (Branch 'e' Empty Empty) Empty)Empty))(Branch 'm' Empty Empty)) (Branch 'u' (Branch 'p' Empty (Branch 's' (Branch 'q' Empty Empty) Empty)) Empty))

-- encode a tree with 2-D coordinates
-- x(v) is equal to the position of the node v in the inorder sequence
-- y(v) is equal to the depth of the node v in the tree
layout :: (Eq a) => Tree a -> Tree (a, (Int, Int))
layout t = encode t
  where refers = inorderTraverse t 1 
        encode Empty                        = Empty
        encode (Branch v lsubTree rsubTree) = Branch (v,(x,y)) (encode lsubTree) (encode rsubTree)
                        where index = fromJust $ findIndex ((==v) . fst) refers
			      (x,y) = (index+1, snd $ refers!!index)
        inorderTraverse Empty                        _ = []
        inorderTraverse (Branch v lsubTree rsubTree) y = inorderTraverse lsubTree (y+1) ++ [(v,y)] ++ inorderTraverse rsubTree (y+1)

-- a more concise solution
-- (1,1) is the top left corner
type Pos = (Int, Int)

layout' :: Tree a -> Tree (a, Pos)
layout' t = fst (layoutAux 1 1 t)
   where layoutAux x y Empty          = (Empty, x)
         layoutAux x y (Branch a l r) = (Branch (a,(x',y)) l' r', x'')
	     where (l',x')  = layoutAux x      (y+1) l
	           (r',x'') = layoutAux (x'+1) (y+1) r

-- an alternative layout method
-- on a given level, the horizontal distance between neighboring nodes is constant
-- tree65 = (Branch 'n' (Branch 'k' (Branch 'c' (Branch 'a' Empty Empty)(Branch 'e' (Branch 'd' Empty Empty)(Branch 'g' Empty Empty)))(Branch 'm' Empty Empty)) (Branch 'u' (Branch 'p' Empty (Branch 'q' Empty Empty)) Empty))
layout2 :: (Eq a) => Tree a -> Tree (a, (Int, Int))
layout2 t = encode t
  where refers = inorderTraverse t (2^(height-1)-1) 1
        encode Empty                        = Empty
        encode (Branch v lsubTree rsubTree) = Branch (v,(x,y)) (encode lsubTree) (encode rsubTree)
                        where index = fromJust $ findIndex ((==v) . fst) refers                             
	                      (x,y) = snd $ (refers!!index)
	inorderTraverse Empty                        _ _ = []
        inorderTraverse (Branch v lsubTree rsubTree) x y = inorderTraverse lsubTree (x-2^(height-y-1)) (y+1) ++ [(v,(x,y))] ++ inorderTraverse rsubTree (x+2^(height-y-1)) (y+1)
	height = getHeight t
        getHeight Empty                        = 0	
	getHeight (Branch v lsubTree rsubTree) = 1 + max (getHeight lsubTree) (getHeight rsubTree)

-- same conventions as the previous question
layout2' :: Tree a -> Tree (a, Pos)
layout2' t = layoutAux x1 1 sep1 t
   where d = depth t
         ld = leftdepth t
	 x1 = 2^(d-1) - 2^(d-ld) + 1
	 sep1 = 2^(d-2)
	 layoutAux x y sep Empty          = Empty
	 layoutAux x y sep (Branch a l r) =
	          Branch (a,(x,y))
		         (layoutAux (x-sep) (y+1) (sep `div` 2) l)
			 (layoutAux (x+sep) (y+1) (sep `div` 2) r)
	 depth Empty          = 0
	 depth (Branch a l r) = max (depth l) (depth r) + 1
	 leftdepth Empty          = 0
	 leftdepth (Branch a l r) = leftdepth l + 1

-- yet another layout strategy
-- tree66 = (Branch 'n' (Branch 'k' (Branch 'c' (Branch 'a' Empty (Branch 'b' Empty Empty))(Branch 'e' (Branch 'd' Empty Empty)(Branch 'g' Empty Empty)))(Branch 'm' Empty Empty)) (Branch 'u' (Branch 'p' Empty (Branch 'q' Empty Empty)) Empty))

layout3 :: (Eq a) => Tree a -> Tree (a, Pos)
layout3 Empty = Empty
layout3 t     = modify $ layoutAux t 0 0
   where 
         layoutAux Empty          _ _     = Empty
         layoutAux (Branch v l r) x y     = Branch (v,(x,y)) 
                                                   (layoutAux l (x-1) (y+1))
                                                   (layoutAux r (x+1) (y+1))
       
modify :: (Eq a) => Tree (a, Pos) -> Tree (a, Pos)
modify Empty    = Empty
modify t        = Branch (v,(x,y)) (modify l) (modify r)
    where (Branch (v,(x,y)) l r)  = modifyAux t 
          modifyAux t'            = stretch t' (subTreeDist t')
                                 

subTreeDist :: (Eq a) => Tree (a, Pos) -> Int 
subTreeDist Empty          = 1
subTreeDist (Branch _ l r) 
     | l == Empty || r == Empty = 1
     | otherwise                = calc l r 
   where
         calc (Branch (lv,(lx,ly)) _ lr) (Branch (rv,(rx,ry)) rl _)
              | lr == Empty || rl == Empty = rx - lx
              | otherwise                  = calc lr rl  
                                             
stretch :: (Eq a) => Tree (a, Pos) -> Int -> Tree (a, Pos)
stretch t@(Branch (v,(x,y)) l r) dist
     | dist > 0  = t
     | otherwise = Branch (v,(x,y)) (add ((dist `div` 2)-1) l) (add (((-dist) `div` 2)+1) r)
   where add _ Empty                    = Empty
         add d (Branch (v,(x,y)) l' r') = Branch (v,(x+d,y)) (add d l') (add d r')
-----------------------------------------------------------------------------------
-- "x(y,a(,b))" <=> Branch 'x' (Branch 'y' Empty Empty) (Branch 'a' Empty (Branch 'b' Empty Empty))

stringToTree :: String -> Tree Char
stringToTree [] = Empty
stringToTree (x:xs) 
    | x == ',' || x == ' ' = stringToTree xs
    | otherwise            = Branch x (stringToTree left) (stringToTree right)
             where (left,right)  = splitAt (parse sub (0, [])) sub
	           parse :: String -> (Int, String) -> Int
		   parse []     _               = 0
	           parse (y:ys) (i, stack) 
		      | y == ',' && stack == [] = i 
		      | y == '('                = parse ys (i+1, stack ++ [y])
		      | y == ')'                = parse ys (i+1, init stack)
		      | otherwise               = parse ys (i+1, stack)
	           sub
	              | xs == []  = []
		      | otherwise = init $ tail $ xs

stringToTree2 :: (Monad m) => String -> m (Tree Char)
stringToTree2 ""  = return Empty
stringToTree2 [x] = return $ Branch x Empty Empty
stringToTree2 str = tfs str >>= \("",t) -> return t
      where tfs a@(x:xs) 
                  | x == ',' || x == ')' = return (a, Empty)
            tfs (x:y:xs)
	          | y == ',' || y == ')' = return (y:xs, Branch x Empty Empty)
		  | y == '('             = do (',':xs', l)  <- tfs xs
		                              (')':xs'', r) <- tfs xs'
					      return $ (xs'', Branch x l r)
	    tfs _                        = fail "bad parse"
treeToString :: Tree Char -> String
treeToString Empty                        = ""
treeToString (Branch x Empty Empty)       = [x]        
treeToString (Branch x lsubTree rsubTree) = [x] ++ "(" ++ treeToString lsubTree ++ "," ++ treeToString rsubTree ++ ")" 

-- solution using Parsec
pTree :: Parser (Tree Char)
pTree = do
    pBranch <|> pEmpty

pBranch = do
    a <- letter
    do char '('
       t0 <- pTree 
       char ','
       t1 <- pTree
       char ')'
       return $ Branch a t0 t1
     <|> do return $ Branch a Empty Empty

pEmpty =
    return Empty

stringToTree3 str =
    case parse pTree "" str of
        Right t -> t
        Left e  -> error (show e)


----------------------------------------------------------------------------------------------------

preOrder :: (Ord a, Show a) => Tree a -> [a]
preOrder Empty                         = []
preOrder (Branch x lsubTree rsubTree)  = [x] ++ preOrder lsubTree ++ preOrder rsubTree

inOrder :: (Ord a, Show a) => Tree a -> [a]
inOrder Empty                          = []
inOrder (Branch x lsubTree rsubTree)   = inOrder lsubTree ++ [x] ++ inOrder rsubTree

postOrder :: (Ord a, Show a) => Tree a -> [a]
postOrder Empty                        = []
postOrder (Branch x lsubTree rsubTree) = postOrder lsubTree ++ postOrder rsubTree ++ [x]

-- construct a tree provided with its preorder and inorder traversal
preInTree :: (Ord a, Show a) => [a] -> [a] -> Tree a
preInTree []     _  = Empty
preInTree (p:po) io = Branch p (preInTree lpo lio) (preInTree rpo rio)
              where (lio,_:rio) = break (==p) io
		    (lpo,rpo)   = splitAt (length lio) po
		    
	        
--------------------------------------------------------------------------------------------------------

-- "abd..e..c.fg..." => Branch 'a' (Branch 'b' (Branch 'd' Empty Empty) (Branch 'e' Empty Empty)) (Branch 'c' Empty (Branch 'f' (Branch 'g' Empty Empty) Empty))

tree2ds :: Tree Char -> String
tree2ds Empty                        = "."
tree2ds (Branch x lsubTree rsubTree) = [x] ++ tree2ds lsubTree ++ tree2ds rsubTree

ds2tree :: String -> Tree Char
ds2tree "." = Empty
ds2tree s   = fst $ aux s
      where aux ('.':xs) = (Empty, xs)
            aux (x:xs)   = (Branch x l r, xs'')
              where (l, xs')  = aux xs
	            (r, xs'') = aux xs'
