-- arguments should be unique
data Tree a = Node a (Tree a)(Tree a)
            | Empty
              deriving (Show)
--a name can appear only once in a set of pattern bindings
--bad_nodesAreSame (Node a _ _) (Node b _ _) = Just a
--bad_nodesAreSame _            _            = Nothing
 
nodesAreSame (Node a _ _) (Node b _ _)
     | a == b    = Just a
nodesAreSame _ _ = Nothing
