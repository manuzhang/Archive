data Li a = Nil | Cons a (Li a)
                  deriving (Eq, Ord, Show, Read)

listLength :: (Li Integer) -> Integer
listLength Nil = 0
listLength (Cons a b) = 1 + listLength b

