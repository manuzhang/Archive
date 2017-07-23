data Tree a = Node a (Tree a)(Tree a)
            | Empty
	      deriving (Show)
	  
simpleTree = Node "parent" (Node "left child" Empty Empty)
                            (Node "right child" Empty Empty)

data Tree2 b = Node2 b (Maybe (Tree2 b))(Maybe (Tree2 b))
               deriving (Show)
simpleTree2 = Node2 "parent" (Just (Node2 "left child" Nothing Nothing))
                             (Just (Node2 "right child" Nothing Nothing))
simpleTree3 = Nothing

treeHeight :: (Tree a) -> Int
treeHeight Empty = 0
treeHeight (Node _ a b) = 1 + max (treeHeight a) (treeHeight b)

treeToList :: Tree a -> [a]
treeToList Empty = []
treeToList (Node n a b) = treeToList a ++ [n] ++ treeToList b

leavesNum :: (Tree a) -> Int
leavesNum Empty = 0
leavesNum (Node n a b) = 1 + leavesNum a + leavesNum b


myTree :: Tree Int
myTree = Node 1 (Node 2 Empty Empty) (Node 3 Empty Empty)

leavesSum :: (Tree Int) -> Int
leavesSum Empty = 0
leavesSum (Node n a b) = n + leavesSum a + leavesSum b

mapT :: (a -> b) -> (Tree a) -> (Tree b)
mapT f (Node n x y) = Node (f n) (mapT f x) (mapT f y)
mapT f Empty        = Empty

