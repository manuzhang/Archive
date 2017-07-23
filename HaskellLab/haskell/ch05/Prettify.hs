{- A pretty-printer's job is to lay out strutured data appropriately 
   The tree Node "foo" (Node "baz" Leaf Leaf) (Node "foobaz" Leaf Leaf) 
   is much easier to read if it is presented as 
      Node "foo" (Node "baz" Leaf Leaf)
                 (Node "foobaz" Leaf Leaf)
-}

module Prettify 
      ( 
        Doc(..)
      , (<>)
      , char
      , double
      , fsep
      , hcat
      , punctuate
      , text
      , compact
      , pretty
      , fill  
      ) where 
      

data Doc = Empty
         | Char Char
         | Text String
         | Line
         | Concat Doc Doc
         | Union Doc Doc
           deriving (Show, Eq)
           
empty :: Doc
empty = Empty

char :: Char -> Doc
char c = Char c

text :: String -> Doc
text "" = Empty
text s  = Text s

double :: Double -> Doc
double d = text (show d)

line :: Doc
line = Line

(<>) :: Doc -> Doc -> Doc
Empty <> y = y
x <> Empty = x
x <> y     = x `Concat` y

hcat :: [Doc] -> Doc
hcat = foldr (<>) empty

fsep :: [Doc] -> Doc
fsep = foldr (</>) empty

(</>) :: Doc -> Doc -> Doc
x </> y = x <> softline <> y

softline :: Doc
softline = group line

group :: Doc -> Doc
group x = flatten x `Union` x

flatten :: Doc -> Doc
flatten (x `Concat` y) = flatten x `Concat` flatten y
flatten Line           = Char ' '
flatten (x `Union` _)  = flatten x
flatten other          = other

punctuate :: Doc -> [Doc] -> [Doc]
punctuate p []      = []
punctuate p [d]     = [d]
punctuate p (d:ds)  = (d <> p) : punctuate p ds

compact :: Doc -> String
compact x = transform [x]
      where transform []     = ""
            transform (d:ds) = 
                case d of 
                  Empty        -> transform ds
                  Char c       -> c : transform ds
                  Text s       -> s ++ transform ds
                  Line         -> '\n' : transform ds
                  a `Concat` b -> transform (a:b:ds)
                  _ `Union` b  -> transform (b:ds)
                  
pretty :: Int -> Doc -> String
pretty width x = best 0 [x]
    where best col (d:ds) = 
               case d of 
                 Empty            -> best col ds  
                 Char c           -> c :  best (col + 1) ds
                 Text s           -> s ++ best (col + length s) ds
                 Line             -> '\n' : best 0 ds
                 a `Concat` b     -> best col (a:b:ds)
                 a `Union` b      -> nicest col (best col (a:ds))
                                                (best col (b:ds))
          best _ _ = ""
          nicest col a b | (width - least) `fits` a = a
                         | otherwise                = b
                         where least = min width col
         
fits :: Int -> String -> Bool
w `fits` _ | w < 0 = False
w `fits` ""        = True
w `fits` ('\n':_)  = True
w `fits` (c:cs)    = (w - 1) `fits` cs        


{- add spaces to a document until it is the given number of columns wide.
   if it is already wider than this value, it should not add any spaces -}

fill :: Int -> Doc -> Doc
fill width x 
    | width <= docWidth         = x 
    | otherwise                 = fill width (x <> (Char ' ')) 
          where docWidth        = getWidth x
                getWidth d      =
                    case d of
                      Empty         -> 0
                      Char c        -> 1
                      Text s        -> length s
                      Line          -> 1
                      a `Concat` b  -> (+) (getWidth a) (getWidth b)
                      a `Union` b   -> if getWidth a >= getWidth b
                                          then getWidth a
                                               else getWidth b
                      
{- 
 - whenever we open parentheses, braces, or brackets, 
 - any lines that follow should be indented 
 - so that they are aligned  with the opening character 
 - until a matching closing character is encountered. 
-}

     