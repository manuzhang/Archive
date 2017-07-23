comprehensive xs ys = [(x,y) | x <- xs, y <- ys]

monadic xs ys = do { x <- xs; y <- ys; return (x,y) }