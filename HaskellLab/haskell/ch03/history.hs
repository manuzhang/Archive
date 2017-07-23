data FunHistory a b = Result b (FunHistory a b)
                    | Application (a -> FunHistory a b) a (FunHistory a b)
                    | Base (a -> FunHistory a b)

instance (Show a, Show b) => Show (FunHistory a b) where
  show (Base _) = "base"
  show (Application _ x h) = "app " ++ show x ++ ", " ++ show h
  show (Result r h) = "result: " ++ show r ++ ", " ++ show h
                             
mkHist :: (a -> b) -> FunHistory a b
mkHist f = let h = Base (\x -> Result (f x) h) 
           in h
              
app :: a -> FunHistory a b -> FunHistory a b
app x (Result _ _)        = undefined
app x (Application f _ _) = f x
app x (Base f)            = f x

mkHist2 :: (a -> a -> b) -> FunHistory a b
mkHist2 f = let h = Base (\x -> mkHist' f x h)
            in h
mkHist' f x h = let h' = Application (\y -> Result (f x y) h') x h
                in h'
                   
mkHist3 :: (a -> a -> a -> b) -> FunHistory a b
mkHist3 f = let h = Base (\x -> mkHist2' f x h)
            in h
mkHist2' f x h = let h' = Application (\y -> mkHist' (f x) y h') x h
                 in h'