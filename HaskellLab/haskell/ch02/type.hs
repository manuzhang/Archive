-- typical examples for type signatures
f :: Int
f = 1

apply :: (b -> b) -> (a -> b) -> a -> b
apply f g x = g x

compose :: (b -> c) -> (a -> b) -> a -> c
compose f g x = f (g x) 
